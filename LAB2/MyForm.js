import React from 'react';
import database from './firebase_config.js';



/*function writeRate(new_rate){
    database().ref('config.json').set({current_rate:new_rate});
}*/


class MyForm extends React.Component{

    constructor(props){
        super(props);
        this.state =
        {
            data: [],
            time: [],
        };
    }

    componentDidMount(){
        let new_data = [], new_time = []
        new_data = this.state.data
        new_time = this.state.time
        database.database().ref(this.props.accel).once("child_added", function(snapshot, prevChildKey){
            let data, time
            
            data = Number(snapshot.val().data);
            time = snapshot.val().timestamp;
            new_data.push(data);
            new_time.push(time);
            console.log("mais um")
        }).then((e) => {
            this.setState({data: new_data, time: new_time})
        })
    }

   /* writeRate(new_rate){
        database().ref('/').set({current_rate:new_rate});
    }*/

    /*mySubmitHandler = (event) => {
        event.preventDefault();
        alert("You are submitting a new update rate value of:" + this.state.new_rate);
        database().ref('config/').set({current_rate: this.state.new_rate});
    }*/

    render(){
       // console.log(this.state)
        //const new_name = "joao";
        return(
            <div>
                
            <h1>Accel_X_Data</h1>
            <p>{this.state.data}</p>
            </div>
           /* <form onSubmit={this.mySubmitHandler}>
                <p>Enter new Update rate:</p>
                <input type='float' onChange = {this.myChangeHandler}/>
            </form>*/
        );
    }
}

export default MyForm;