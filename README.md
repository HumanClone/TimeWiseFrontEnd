![Static Badge](https://img.shields.io/badge/Kotlin-Kotlin?label=Language&labelColor=purple) ![Static Badge](https://img.shields.io/badge/MIT-License?label=License&labelColor=blue) ![Static Badge](https://img.shields.io/badge/Android-Platform?label=Platform)</br>

# TimeWise

Timewise is an Android application which allows users to log their hours spent on various categories and assign themself a daily maximum and minimum.

This app makes use of e material design as well as Firebase for cloud storage infrastructure.
Target is using Android Tiramisu `(API 33)`



#### API

[![API](https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/f3f34ecb-b380-4a41-97c4-3f7031ce95be)](https://github.com/HumanClone/TImeWiseBackEnd)

This project uses an API to faciliatate data entry and retrival

***



## Features

- Account Creation
- Category Creation
- Time Sheet Creation
- Setting a maximum and miminmum daily goal
- Optional Picture upload
- Calendar View
- Dynamic theme
- Dynamic Graph
- Searching functionality
- Modal Views that have intutitve uses
- Export  data as a `.csv `

***

## Application Walkthrough

### Register and Login

`Register`
-
User can simple enter thier details and create an account on the database


`Login`
-
User can simply enter thier credentials to log into the application

### Creating A Category 

User can click button to create a new category  


### Creating A Time Sheet

User can create a Time Sheet in which they can choose whether they want to upload a picture or not

`Entry Points`

<div>
Time Sheet
Category
Calendar
</div>



### Full View of a Time Sheet
User can click on any Time Sheet item to view the full details 

`Entry Points`
<div>
Dashboard
Time Sheet
Category 
</div>

<div>
Statistics(Time Sheet)
Statistics(Category)
Calendar
</div>

### Exporting Data 
By selecting the button the application will create a .csv file containing information about all the Time Sheets. This file can then be saved onto the devices storage system and then opened in a spreadsheet or an appropirate app

### Searching
Here the user can search and filter timesheets or categories based on either a time period, a category or both. if they choose to do a time period for categoies they only see the total hours in that category for the selected period 

### Graph
This is a dynamic graph that displays the total hours logged on days by the user either in thier entirity or for a selectable period
To see a detailed value of each point simply click on it 

Hide and show data

dynamic shaping 

### Calendar
This shows all dates that contain a Time Sheet in the form of a calendar with the feature of viewing and even creating a new entry on a date

### Dynamic Theme

The theme of the application is set automatically based on the user's system theme, but there is a manual switch to change it as well

***

## App Interface

<div>
Dashboard
Profile
Category 
</div>
<div>
Time Sheet 
Create Time Sheet
View Time Sheet
</div>

<div>
Statistics
Calendar
Graph 
</div>

***

## Dependencies

This project makes use of the following:

- [Material Design V3](https://m3.material.io/)
- [AAChartCore](https://github.com/AAChartModel/AAChartCore-Kotlin)
- [Crunchy Calendar](https://github.com/CleverPumpkin/CrunchyCalendar)
- [Firebase](https://firebase.google.com/retr)
- [Retrofit2](https://square.github.io/retrofit/)
- [GSON](https://github.com/google/gson) 

***

## Requirements

>- Firebase authentiction
>- Android Tirimasu (API 33)
>- Google Servcies

## How to Run

>>Download APK provided
or
Download repository zip and ope in Android Studio and build and run
***

## Change Log

### Version 1.1


> - Fixed bugs that led to crashes
> - Updates Dashboard with a view on percentage of goals accomplished in the last 30 days
> - Major visual redesign and updates
> - Added Calendar view
> - Added Dynamic Graph
> - Added Manual Theme Change 


***

## End Notes


***

## License

```en
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
