importScripts('https://www.gstatic.com/firebasejs/5.5.5/firebase.js')
// Initialize Firebase
var config = {
    apiKey: "AIzaSyDkGKXxs-SsX7cfOdX6XND2lb1LAQ3u8FI",
    authDomain: "userapplication-f0a2a.firebaseapp.com",
    databaseURL: "https://userapplication-f0a2a.firebaseio.com",
    projectId: "userapplication-f0a2a",
    storageBucket: "userapplication-f0a2a.appspot.com",
    messagingSenderId: "200004929613"
};
firebase.initializeApp(config);
const messaging = firebase.messaging();