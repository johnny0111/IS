import React , { Component } from 'react';
import firebase from './firebase_config.js';
import Chart from './Chart.js';
import Update_rate from './Update_rate.js';
import 'firebase/auth';
import { StyledFirebaseAuth } from 'react-firebaseui'

class Login extends Component {
  state = { isSignedIn : false }

  uiConfig = { 
    signInFlow: "popup", 
    signInOptions: [ 
      firebase.auth.GoogleAuthProvider.PROVIDER_ID, 
    ], 
    callbacks: { signInSuccess: () => false } 
  }

  componentDidMount = () => { 
  firebase.auth().onAuthStateChanged(user => { 
   this.setState({ isSignedIn: !!user }) 
   console.log("user", user) 
 }) 
  }
  render(){
    return (
      <div className="App">
        {this.state.isSignedIn ? (
            <span>
              <div>Signed In!</div>

                <button onClick={() => firebase.auth().signOut()}> Sign out! </button> 

                <h1>Welcome {firebase.auth().currentUser.displayName}</h1> 

                <img alt="profile picture" src={firebase.auth().currentUser.photoURL} /> 

                <Update_rate/>
                <p>DATA_X</p>
                <Chart name='accelX'/>
                <p>DATA_Y</p>
                <Chart name='accelY'/>
                <p>DATA_Z</p>
                <Chart name='accelZ'/>

              </span> ) : ( 

              <StyledFirebaseAuth uiConfig={this.uiConfig} firebaseAuth={firebase.auth()} /> 
          )
          
          }
      </div>
    );
  }
}

export default Login;
/** <MyForm
      accel="accelX"/>
      
      <Update_rate/>
     <p>DATA_X</p>
     <Chart name='accelX'/>
     <p>DATA_Y</p>
     <Chart name='accelY'/>
     <p>DATA_Z</p>
     <Chart name='accelZ'/>
     <Logout/>
      */


