import React from 'react';
import firebase from './firebase_config.js';


class MyForm extends React.Component{

    constructor(props){
        super(props);
        this.state ={new_rate: ''};
    }

    mySubmitHandler = (event) => {
        event.preventDefault();
        alert("You are submitting a new update rate value of:" + this.state.new_rate);
        firebase.database().ref('config/').set({current_rate: Number(this.state.new_rate)});
    }

    myChangeHandler=(event) => {
        this.setState({new_rate:event.target.value});

    }

    render(){
        return(
            <form onSubmit={this.mySubmitHandler}>
                <p>Enter new Update rate:</p>
                <input type='float' onChange = {this.myChangeHandler}/>
                <p>{this.state.new_rate}</p>
            </form>
        );
    }
}

export default MyForm;