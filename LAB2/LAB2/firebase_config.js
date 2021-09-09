import firebase from "firebase/app";
import 'firebase/auth';
import "firebase/database"

const firebaseConfig = {
    apiKey: "AIzaSyD1RhaMI459djhnZTwpZiXW9F7p1NIalmk",
    authDomain: "is-tp2-84eaf.firebaseapp.com",
    databaseURL: "https://is-tp2-84eaf-default-rtdb.europe-west1.firebasedatabase.app",
    projectId: "is-tp2-84eaf",
    storageBucket: "is-tp2-84eaf.appspot.com",
    messagingSenderId: "270404015852",
    appId: "1:270404015852:web:43510a12688adebab4f7c9"
};
firebase.initializeApp(firebaseConfig);
export default firebase;
