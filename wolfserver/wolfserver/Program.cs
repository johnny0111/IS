//using System;
//using System.Net;
//using System.Net.Sockets;
//using System.Threading;
//using System.Text;
//using System.Xml.Serialization;
//using System.IO;

//namespace wolfserver
//{
//    class Program
//    {
//        public static int Main(String[] args)
//        {
//            StartServer();
//            return 0;
//        }


//        public static void StartServer()
//        {
//            //    // Get Host IP Address that is used to establish a connection  
//            //    // In this case, we get one IP address of localhost that is IP : 127.0.0.1  
//            //    // If a host has multiple addresses, you will get a list of addresses  
//            //    IPHostEntry host = Dns.GetHostEntry(Dns.GetHostName(););
//            //    IPAddress ipAddress = host.AddressList[0];
//            //    IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 4444);


//            //    try {

//            //        // Create a Socket that will use Tcp protocol      
//            //        Socket listener = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
//            //        // A Socket must be associated with an endpoint using the Bind method  
//            //        listener.Bind(localEndPoint);
//            //        // Specify how many requests a Socket can listen before it gives Server busy response.  
//            //        // We will listen 10 requests at a time  
//            //        listener.Listen(10);


//            //        Console.WriteLine("Waiting for a connection...");
//            //        Socket handler = listener.Accept();


//            //        // Incoming data from the client.    
//            //        string data = null;
//            //        byte[] bytes = null;

//            //        while (true)
//            //        {
//            //            bytes = new byte[1024];
//            //            int bytesRec = handler.Receive(bytes);
//            //            data += Encoding.ASCII.GetString(bytes, 0, bytesRec);
//            //            if (data.IndexOf("</tMyPlace>") > -1)
//            //            {
//            //                break;
//            //            }

//            //        }
//            //        XmlSerializer ser = new XmlSerializer(typeof(tMyPlace));
//            //        tMyPlace pl = new tMyPlace();
//            //        StringReader str = new StringReader(data);
//            //        pl = (tMyPlace)ser.Deserialize(str);
//            //        for (int i = 0; i < pl.Place.Length; i++)
//            //        {
//            //            if (pl.Place[i].Cow  == true && pl.Place[i].Obstacle == false)
//            //            {
//            //                pl.Place[0].Position.xx = pl.Place[i].Position.xx;
//            //                pl.Place[0].Position.yy = pl.Place[i].Position.yy;
//            //                pl.Place[0].Wolf = true;
//            //                pl.Place[0].Grass = --pl.Place[0].Grass;
//            //            }
//            //        }
//            //        //serialization
//            //        StringWriter textWriter = new StringWriter();
//            //        ser.Serialize(textWriter,pl);
//            //        byte[] msg = Encoding.ASCII.GetBytes(textWriter.ToString());
//            //        handler.Send(msg);
//            //        handler.Shutdown(SocketShutdown.Both);
//            //        handler.Close();
//            //    }
//            //    catch (Exception e)
//            //    {
//            //        Console.WriteLine(e.ToString());
//            //    }

//            //    Console.WriteLine("\n Press any key to continue...");
//            //    Console.ReadKey();
//            TcpListener server = new TcpListener(IPAddress.Any, 4444);
//            // we set our IP address as server's address, and we also set the port: 9999





//        }
//    }
//}

using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text;
using System.Xml.Serialization;
using System.IO;
using System.Xml;

namespace wolfserver
{
    class Program
    {
        public static int Main(String[] args)
        {
            StartServer();
            return 0;
        }
        public class Utf8StringWriter : StringWriter
        {
            public override Encoding Encoding => Encoding.UTF8;
        }

        public static void StartServer()
        {
            // Get Host IP Address that is used to establish a connection  
            // In this case, we get one IP address of localhost that is IP : 127.0.0.1  
            // If a host has multiple addresses, you will get a list of addresses  
            IPHostEntry host = Dns.GetHostEntry("localhost");
            IPAddress ipAddress = host.AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 4445);

            TcpListener server = new TcpListener(IPAddress.Any, 4445);
            server.Start();
            try
            {
                while (true)
                {
                    Console.WriteLine("Waiting for a connection...");
                    TcpClient client = server.AcceptTcpClient();//accept connection
                    NetworkStream ns = client.GetStream();//send/recieve messages

                    Console.WriteLine("Connected");

                    string data = null;
                    byte[] bytes = null;




                    while (true)
                    {
                        bytes = new byte[1024];
                        ns.Read(bytes, 0, bytes.Length);
                        data += Encoding.ASCII.GetString(bytes, 0, bytes.Length);
                        Console.WriteLine("preso");
                        int bytes_read = data.IndexOf("</MyPlace>");

                        if (bytes_read != -1)
                        {
                            int length = bytes_read - 0 + 10;
                            data = data.Substring(0, length);

                            break;
                        }
                    }

                    Console.WriteLine("\nMessage recived");

                    XmlSerializer ser = new XmlSerializer(typeof(tMyPlace));
                    string utf8;
                    tMyPlace pl = new tMyPlace();
                    StringReader str = new StringReader(data);
                    pl = (tMyPlace)ser.Deserialize(str);

                    Console.WriteLine("\nDeserialization Done!!");

                    for (int i = 0; i < pl.Place.Length; i++)
                    {
                        if (pl.Place[i].Cow == true && pl.Place[i].Obstacle == false)
                        {
                            pl.Place[0].Position.xx = pl.Place[i].Position.xx;
                            pl.Place[0].Position.yy = pl.Place[i].Position.yy;
                            pl.Place[0].Wolf = true;
                        }
                    }
                    Console.WriteLine("\n New place calculated.");

                    //data = null;
                    //TextWriter writer = new StreamWriter(@ "D:/joaom/Documents/Mestrado/2º semestre/IS/wolfserver/wolfserver/myPlaceSchema.xml.xsd");
                    //TextWriter writer = new StreamWriter("D:\\joaom\\Documents\\Mestrado\\2º semestre\\IS\\wolfserver\\wolfserver\\myPlaceSchema.xml.xsd");
                    //ser.Serialize(writer, pl);

                    //Console.WriteLine("\n SErializatioon done");
                    //String s = writer.ToString();


                    using (StringWriter writer = new Utf8StringWriter())
                    {

                        XmlWriterSettings setting = new XmlWriterSettings();
                        setting.Indent = false;
                        setting.NewLineHandling = 0;
                        XmlWriter xmlWriter = XmlWriter.Create(writer, setting);
                        xmlWriter.WriteStartDocument(true);

                        ser.Serialize(xmlWriter, pl);
                        utf8 = writer.ToString();
                        byte[] msg = Encoding.ASCII.GetBytes(writer.ToString());
                        ns.Write(msg);
                        writer.Close();
                        ns.Close();
                    }
                    //StringWriter writer = new StringWriter();
                    //XmlWriterSettings setting = new XmlWriterSettings();
                    //setting.Indent = false;
                    //setting.NewLineHandling = 0;
                    //setting.Encoding = System.Text.Encoding.UTF8;
                    //XmlWriter xmlWriter = XmlWriter.Create(writer, setting);
                    //xmlWriter.WriteStartDocument(true);
                    //ser.Serialize(xmlWriter, pl);
                    //byte[] msg = Encoding.ASCII.GetBytes(writer.ToString());
                    //ns.Write(msg);
                    //writer.Close();
                    //ns.Close();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

                Console.WriteLine("\n Press any key to continue...");
                Console.ReadKey();

        }


    }
}
