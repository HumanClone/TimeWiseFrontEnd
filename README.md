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

<img src='' width='24%'>

`Login`
-
User can simply enter thier credentials to log into the application

<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/a514af0a-994a-47b1-af82-6bcfc066652e' width='24%' >

### Creating A Category 

User can click button to create a new category  

<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/75d08c0c-7697-44f9-83e4-add7804dfda8' width='24%'>

### Creating A Time Sheet

User can create a Time Sheet in which they can choose whether they want to upload a picture or not
</br>

|WIth Picture | Without Picture |
|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/ae969714-b6c4-4b82-b0ca-6ba766bd1315'> | <img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9aac0275-5b2b-457e-b5fc-44fde9c16904'>|
</br>
</br>

`Entry Points`

|Time Sheet | Category  | Calendar| 
|:---------:|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9bc20c4c-444f-406c-889a-8aaf2cf8a619' width='70%'> | <img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/2c9e4b03-5dc5-4664-a92e-307afdc8a55f' width='70%' >|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d1804154-bdcf-41e2-ad2d-3123ab244a7c' >|

### Full View of a Time Sheet
User can click on any Time Sheet item to view the full details
</br>
<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d9a1654f-2286-4953-978d-4672accb515a' width='24%'>
</br>
</br>
`Entry Points`
| Dashboard |Time Sheet  | Category | 
|:---------:|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/6748a8ea-36e9-42ff-9e7f-33db882ccef0'> | <img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/27e93673-9ff7-421e-88f9-8d217235e020' width='70%'>|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/a47b4ed3-9787-4f0c-a4cc-1979dfff2d29'>|
</br>


| Stats(Time Sheet) |Stats(Category)  | Calendar | 
|:---------:|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/1cece018-d5b3-48e6-a63b-f0f1e251f69b' width='70%' > |<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/4631b519-eec4-4dea-9cc9-3e536c1cde06'> |<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d72f556f-d4b5-4a64-b237-5b24d9ea8b05'>|
</br>


### Exporting Data 
By selecting the button the application will create a .csv file containing information about all the Time Sheets. This file can then be saved onto the devices storage system and then opened in a spreadsheet or an appropirate app

<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/db941cbd-e6df-4fce-8aaa-1df6dedcfd15' width='24%'>

### Searching
Here the user can search and filter timesheets or categories based on either a time period, a category or both. if they choose to do a time period for categoies they only see the total hours in that category for the selected period 

`Time Sheet`
-

|Date |Category  | Both | 
|:---------:|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/0ffacabe-8ed8-4269-a1eb-cfb143d06a44'> | <img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/38b0d572-255b-4ef2-a15e-4958a1b0c01e' width='70%' >|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/78683e02-ed5a-4378-a479-26cb3d3ddcdb' >|


`Category`
-

|Date |Category  | Both | 
|:---------:|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/2d3f678f-e028-4fb9-b9f6-66141d9586b8'> | <img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/14da6fe4-d2d5-4a96-a248-03404a2afd1c' width='70%' >|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/13f0bc1f-6006-44ad-8da0-80c4037380e6' >|



### Graph
This is a dynamic graph that displays the total hours logged on days by the user either in thier entirity or for a selectable period
To see a detailed value of each point simply click on it 

| Interaction | Filtering |
|:---------:|:---------:|
|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/cfdfa42a-8994-4caa-903e-d9c161b18ae0' >|<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/fe3ae066-5e5f-43d9-92f0-3b6b371a605b' width='200%'>|


### Calendar
This shows all dates that contain a Time Sheet in the form of a calendar with the feature of viewing and even creating a new entry on a date
<img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d99d877d-0391-4c46-9e64-9859190034ef' width="24%" >
### Dynamic Theme

The theme of the application is set automatically based on the user's system theme, but there is a manual switch to change it as well
<img src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/76ecc4f9-4ff0-4d89-86ec-d44c4d8dbe56' width='24%'>

***

## App Interface

| Dashboard | Profile  | Category | 
|:---------:|:---------:|:---------:|
|<img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/a2aafa40-7fd5-49ac-9ea9-9c7ada11ed5b' >| <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/73c326f3-006d-4dbc-a67c-7db53400ae1c'  >|<img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9fd4f41f-009c-464c-af95-7ecb4e4ddf7b'  >|

</br>

| Time Sheet | Create Time Sheet  | Time Sheet View  | 
|:---------:|:---------:|:---------:|
| <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/66d849ba-f8af-4139-bf54-1ec66c60af5a' width="200%" >   |  <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/a9af3cc7-2529-4484-85e8-b43e30267e4e' width="200%" > | <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/15954629-d6b8-4e2f-9770-79348d75188b' width="200%" >  |

</br>

| Statistics | Calendar | Graph    | 
|:---------:|:---------:|:---------:|
| <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/31add933-db9d-4876-be53-06b3d7042972' >   |  <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/d99d877d-0391-4c46-9e64-9859190034ef' width="200%" > | <img  src='https://github.com/HumanClone/TimeWiseFrontEnd/assets/74468682/9fc03cc4-8a8c-4e69-8d9b-a467cc9b9b4e' width="200%" >  |

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
