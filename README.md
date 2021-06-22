# Android Flight Gear Controller
#### Created by:
- Matan Saloniko
- Idan Givati

## Project description
This app is fairly simple.

In this project, our goal was to create an app that lets you connect to FlightGear and control a flight simulation.

When you open the app you're greeted with a clean and simple screen that lets you enter the IP and port of the FlightGear server you've created. (If you are not familiar with FlightGear and how to create a server, do not worry, we will cover that later). 

![App startup screen](https://i.imgur.com/4CJ5FZA.png)

In the case that you've entered the wrong IP or port and the app failed to connect, you will be presented with an error message.

![Failed to connect](https://i.imgur.com/0DJ1Tqa.png)

After entering the correct IP and port, the app can finally connect to the server and you will be presented with the controllers.

### A short description about the controls:
- To the left, you'll see the throttle controller that lets you adjust the throttle of the airplane.
- Right at the bottom you'll see the rudder controller
- At the center of the screen you'll see the joystick that lets you adjust the aileron and elevator.

![Controls](https://i.imgur.com/SOOdnfJ.png)

You can click the "DISCONNECT" button to disconnect from the current sever and connect to a new one if you wish to do so.

![Disconnect](https://i.imgur.com/1cDL2pi.png)

If you wish to see more of this project in action, you can view the short demonstration video we've created!

Or, better yet, just follow the instructions below to install the app and control the flight on your own!

## Requirements

- FlightGear is necessary in order to run the the application correctly.
Download links:
https://www.flightgear.org/download/ 

- The app (.apk) or the project is also required. You can clone the repo or download the apk file here: [.apk file](https://drive.google.com/file/d/1bE9usfPDbVgVrrLeUSwbsul3olTQjjkB/view?usp=sharing)
If you chose to clone the repo, you'll have to install Android Studio in order to run it properly. You can do that here: https://developer.android.com/studio

## Project structure

In this project we focused on the MVVM architecture, that could be seen a lot throughout the code.

![UML](https://i.imgur.com/xiMFBIT.png)

As you can see in the UML above we created a Joystick class that is completely independent and can be implemented in other projects without changing the class itself.

We have the MainActivity class which is the View of this project, it uses data binding and event changes with the ViewModel class, which is, unsurprisingly, the view model of this project.
The ViewModel class communicates with the FlightGearPlayer class which is the model of this project that handles the server connection and sending the data.


## Running the project

To run the project, you can use one of 2 methods:

### Method 1: The user friendly(er) way:

 - First of all, you'll have to install FlightGear using the link provided above.

 - After running FlightGear, you will be presented with this screen:
 ![FlightGear startup](https://i.imgur.com/WLMJeyn.png)

- Go into the Settings panel, scroll down and enter the line below as shown in the screenshot

    --telnet=socket,in,10,127.0.0.1,6000,tcp

![FGSettings](https://i.imgur.com/iADawVl.png) 

- Click on "Fly!"
- Open cmd or terminal on your machine and type the following command:

    For windows:
    ipconfig

    For Linux/MacOS:
    ifconfig

- Check the IP shown next to "IPv4 address" and save it. We'll use that in a bit.

Now, after we dealt with the FlightGear side of things, let's install and run the app!

- Download the .apk file from the link given: [.apk file](https://drive.google.com/file/d/1bE9usfPDbVgVrrLeUSwbsul3olTQjjkB/view?usp=sharing)
- Install the app on your Android phone (if you don't know how to do so, you can use this guide: https://www.howtogeek.com/313433/how-to-sideload-apps-on-android/ )
- Open the app
- Enter the IP you got from your machine a few steps back in the IP field
- In the "Port" field, enter 6000
- Click "Connect"
- Go to FlightGear, click the "Cessna C172P" tab and click "Autostart"
- Enjoy! You can now control the plane!

### Method 2 (The developer way):

As for the FlightGear setup, it is the exact same as method 1. 

The difference is how to get the app running.

- Clone the repo
- Open the cloned repo in Android Studio
- Run the project on an emulator 
- Insert the IP and port (you can see further instructions under "Method 1").
- Enjoy!

Side note: if you don't know how to run an emulator on your machine, you can follow this guide:
https://developer.android.com/studio/run/emulator


