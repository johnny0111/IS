import React, { Component, useEffect, useState } from "react";
//import { Line } from 'react-chartjs-2';
import database from './firebase_config.js';
//import Recharts from "./RechartsChart.js";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';


const Chart = (props) => {
    
    const [data, setData] = useState([]);
    //const list = []
    useEffect(() => {
        database.database().ref(props.name).on('child_added', function (childSnapshot, prevChildKey) {
            const pos = childSnapshot.val();//Se chamar uma vez dá o primeiro valor 
           //appending data to array currentData
            setData(currentData => [...currentData, pos]);
            //console.log(pos)
        });
    },  []);
    //console.log(data)
    return (
        <div>
            <ResponsiveContainer width='90%' height={300}>
                <LineChart data={data}>
                    <XAxis />
                    <YAxis/>
                    <CartesianGrid stroke="#eee" strokeDasharray="5 5"/>
                    <Line dataKey={'data'}/>
                </LineChart> 
            </ResponsiveContainer>
               
        </div>
    );
}; 
export default Chart;