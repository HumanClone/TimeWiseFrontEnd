# TimeWiseFrontEnd
This is the repository that we will use for the android application 
This is TimeWise a alpha version of a work/time tracker in which you can log and track your daily hours in a specific category and then you it for you own purposes.
This app makes use of Google's native material design as well as thier cloud storage infrastructure such as firebase. Target is using Android Tiramisu, 


# APP Walkthrough

## Register and Login 

### Register
Here the USer can Register for the App by provideing thier details 

![Register](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9f011fd0-11c7-4caa-ae0d-6fe19472b89c)
<br>
<br>
### Login 
User will enter thier credentials which will be validated and upon success be signed into that app, which causes the app to load the needed data from the database through the api 

![Login](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d7ae365a-6300-4875-9985-0a09f8f0f109)
<br>
<br>
## Main Menu with Appdrawer Fragments
from here the user is led to the dashboard of the app in which if they had any data willbe populated on to the screen else the screen will not display anything and they can us the rest of the app and create user data 

![Main_Menu](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9e6417ed-9a27-4c53-a366-910cb20ba918)


### Dashboard
this fragment begins with the loading of the last 5 timesheets and categoriesof the user plus total hours in each category 

![Dashboard](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/45b8d354-1717-4a1c-8579-c0d692cb05df)


### Profile 
here the user can view thier details and change the max and min daily hours on thier profile 

![Profile](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/396b23ad-388b-45a4-92ed-d8b2a0c899f0)


### Category 
Here the user will be able to view all categories of thiers and create a new category by tapping the button 

![Category ](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/5f774a7e-fa3f-4b2e-8d32-1b97e6c66682)


### TimeSheet 
here the user will be able to view their own timesheets in certain criteria = eg, week ,month and all. which is done by selecting the buttons on the screen however when the fragment is first loaded the user will see the timesheets in the current week <br>

![TimeSheet](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d43c6db0-9360-4fe0-9995-ccbcf031dcd5)

### Single TimeSheet View 
this view creates itself when a user clicks on a timesheet entry, in rder to better view the details and picture of the entry 


### Create TimeSheet
here the user can enter the details and create a sheet with an optional picture that would be uploaded to firebase as well and will be saved it the timesheet and shown in any view of the timesheet but in proportion to the selected view 


![CreateTS](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/a2671eb2-c88b-4ad6-8fb9-c5e2f82f7922)


### Statistics 
here the user willbe able to search through either the categories or timesheets based on the tab selected as well as search either a date range or category or both 
if the user wishes to create a new timesheet they need to click the new sheet button and will be take to the screen to create a new sheet 

![Stats](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/0d31f324-381f-4c7b-bec2-4affd32b99d6)

## Logout 
here the user will be able to logout from the app and reset the user data on them 


# Requirements and Dependencies 
Android 13 API level 33 <br>
Needs to have the firebase auth, storage and real-time SDKs<br>
Android device/emulator must have access to the internet as well as Google Services 
### Device Requirements
4GB of Ram <br>
at least 1GB of storage for application<br> 
Android 13 API level 33<br>
Google Services

# End notes 
The application does have a dark theme that is triggered by the system default theme whether it is light or dark 
further versions will improve on the visual with more data tracker and comparisons being displayed on the dashboard 


# Run App
To run the app you can either build the apk from android studio and load that onto your device or build/compile the application and lauch on android emulator




