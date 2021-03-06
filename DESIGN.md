# Design

## Advanced sketch

![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/WhatsApp%20Image%202018-01-09%20at%2011.21.11.jpeg)

## Utility modules, classes and functions

![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/hoi.png)

## Other actions in the app

* Save recordings to documents on the mobile phone.
* Open saved recordings to play the file.
* Use menu items to change fragments.

## API's and frameworks

* [GENIUS API](https://genius.com/api-clients). I will use this API to search for song and artists. I get a maximum of 10 results back, containing among others the name, the artist and the url to the webpage with lyrics. If the user clicks on the song I will strip this html page until I only keep the lyrics.
* [MediaRecorder](https://developer.android.com/guide/topics/media/mediarecorder.html) on the device. I will use this to record the users voice when he is using the app to make a cover.
* [MediaPlayer](https://developer.android.com/guide/topics/media/mediaplayer.html) on the device. I will use it to play the recorded music.
* Internet. I will use it to use the GENIUS API.
