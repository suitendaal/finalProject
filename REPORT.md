# Loop Station

## Description

In this app you can record some samples to cover a song. There is also an option to search lyrics of a song so it is easier to cover a song.
When a song is finished you can save your samples.

### Screenshot


<img src="https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(5).jpeg" width="30%" height="30%" />

## Technical design

### Overview

![](https://github.com/suitendaal/finalProject/blob/master/doc/FinalReport.jpg)

The application starts in the sandbox mode. The only things you can do that do something actually is go to the menuoptions or press the round record button.
When you press the record button the button's color turns into red. The app starts recording. Pressing the record button again or the squared stop button stops recording.
Congratulations! Your first sample is recorded. The app automatically plays this sample and when it is finished it plays it again.

Now you can use all of the buttons. The pause button pauses the sample, the play button plays the sample and the stop button stops the sample.
When you press the record button again to record the next sample it waits until the sample is finished playing. 
When the color is turned red recording is started. You can stop recording by pressing the record button again, the pause button, the stop button or the save button.
If the samples are not playing and you press the record button the samples automatically start playing.
In the end when the song is completed you can press the save button. The app asks you how you want to name it and when the name is available your recording is saved and can be found under 'My recordings'

In the search mode you can search songs to show the lyrics of this song. By pressing a song the sandbox mode is opened, but now the lyrics are shown and the title of the song.

The my recordings mode shows your own recorded songs. When you press a song you can listen to it once or loop it. When you longpress a song you can delete the song.

The about mode show information about the app.

### Details

#### AboutActivity

Simple activity with only text which tells the user something about the app.

#### ActionChecker

Class with 3 states and a listener interface for the states. It is used for example for the recordbutton to tell when to start recording, when the samples have stopped playing.

#### AlertDialogCreator

Class in which most of the code is put to create an AlertDialog. It has interfaces for the positive and negative button. It is used when the user saves a record or when the user wants to delete a record. The user has to pass an AlertDialog.Builder for the layout. The user can set a positive and a negative button and a title.

#### DirectoryCreator

Class which can create a directory on the mobile device. It creates the directory on the path it is given. This class is used when the app starts to create the loopStation directory and the myRecordings directory and when the user saves a record a directory is created to save the samples.

#### FileSaver

Class which creates an AlertDialog with the AlertDialogCreator to ask the user on what name he wants to save his record. It also checks if the filename given by the user is available. A filename is available if it is not empty and if the filename doesn't exist already.
When the filename is available it passes the filename to the SongCreator to create a song.

#### LyricSetter

Class which gets the html in a string from the genius website. You have to pass an url. When the html is received the class parses it until only the lyrics are left. Then the lyrics are put in the TextView in the MainActivity so the user can see the lyrics.

#### MainActivity

Activity where the user can record samples. The user can record, play, pause, stop and save samples. If the user reached this activity via the SearchActivity the lyrics of the searche song are shown.
Like said before in the beginning the only thing the user can do is record a sample. When a sample is recorded the user can play, pause, stop and save it via the buttons.
When the user presses the record button to record a new sample the previous recorded samples are played so the user can listen when to sing. Recording starts when the samples have finished playing.
When pressing the save button the FileSaver is asked to ask the name of the file.

#### MenuOption

Class which tells the application which activity to start when a menuoption is pressed.

#### MenuVisibility

Class which tells the menu which menuoptions to hide. For example when you are in the SearchActivity the menuoption search is hidden.

#### MyRecordingsActivity

Activity where the user can listen to his recordings. On click a PlayFragment opens and the recording is played. A click plays all the samples in the recording's directory.
On long click the AlertDialogCreator is called to ask if the user wants to delete this song.
If the recording's directory is empty and the user clicks on it there will also be asked if the user wants to delete this song.

#### PlayFragment

Fragment to play a recording. It has 2 togglebuttons, one to play and pause the recording and one to loop the recording. 
When the loop button is pressed the loop mode is on and when the song is finished the song starts playing again.
It opens all the samples which are in the directory and plays them all at the same time via android's mediaplayer.

#### Record

A Record is a class which has an arraylist of samples. It can record a new sample via android's mediarecorder and play all it's samples via android's mediaplayer.
It is controlled via the buttons in MainActivity. It has functions to start and stop recording, to play, pause, stop and loop the samples and to save the recording.
When the save button is pressed it is passed through to the FileSaver.

#### RecordingsAdapter

Adapter which shows all the recorded files in the listview in MyRecordingsActivity. It also passes a tag to the textview so the onclick function knows where the file is stored.

#### Sample

A Sample is a mp4 file which is recorded and saved on de mobile device. It has functions to play, pause and stop which are passed from the Record class.

#### SearchActivity

Activity where the user can search songs to show the lyrics from in the MainActivity. When the user types in a character it automatically does a new request to the genius api.
The (maximum 10) results are shown in a listview. When a user clicks on a result the MainActivity starts and it shows the lyrics of this song.

#### Song

A Song is a result from the SearchActivity. It is a simple class which has a songtitle, an artist and an url to the genius website.

#### SongAdapter

The SongAdapter is an adapter which shows the song results in the listview in SearchActivity. It shows the song title and the artist and it passes the url as a tag.

#### SongCreator

The SongCreator is the class which saves the recording. It gets the filepath and the samplefiles passed and then it creates a new directory on the filepath and puts the samples in this created directory.

### Challenges and changes

#### Start recording

When you have recorded the first sample I wanted to press the record button whenever you liked. I found out it was easier to mix audio when the recordings all started at the same moment. So when you press the record button the application waits with recording until the loop has ended.

#### Record a new sample

When you record a new sample I wanted to overwrite the previous sample with the new mixed sample. I found out it was pretty difficult to mix audio so I decided to record all samples in different files and when you click play all of the samples are played at the same time.

#### Save a file

When the user presses the save button I wanted to collect all the samples and merge them to one audio file. I found out it was difficult and beyond my knowledge and the timespan of this project to find out how to merge audio files. I decided when the user picks a name a directory is created with this name and all of the samples are put into this directory. When the user plays a recording all of the samples are played at the same time.

#### Fragments or activities

I wanted to make every app page a fragment for the smoothness of the application. But working with fragments is a little bit different than with activities. When I started programming I thougth let's not make it too difficult with bugs and errors caused by the fragment so I decided to use activities, which are easier to programm.

#### More time

If I had more time for this project I would find out how to merge recorded audio files so a recording can be shared as audio file. There is also a short delay on the recording, which is quite frustrating when you want to record. Maybe I would build in a delay in playing or recording to prevent this delay.
