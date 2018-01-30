# LoopStation

Made by Sven Uitendaal.

## Problem statement

A composer composing music is often on his own. On your own you can not hear all of the music at the same time you are composing.
The problem is it would be better if a composer can hear his composed music right when he conceived it, so he can check if the music sounds like how he ment to sound it.


## Solution

The solution to this problem is an application which works like a loop, where the user can record his own voice several times.

#### Visual sketch

![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/IMG_20180108_135306.jpg)

#### Main features

The minimum viable prdoduct features are marked bold and italic.
* ***Record your voice.***
* ***Loop your voice.***
* ***Save your recorded song.***
* ***Listen to your recorded songs.***
* Choose a song you want to cover. 
* Show the song's lyrics.
* Use a metronome.


## Prerequisites

#### Data sources

I will use the [GENIUS](https://genius.com/api-clients) API to search songs and artists. It will return a JSON response with information like song title, artists and, important for this app, an url for the webpage where I can get the lyrics from by stripping the html.

#### External components

I want to use the audio recorder and I want to save the records.

#### Similar mobile apps

* LoopStation - Looper: This application turns your android device into a musical instrument. Record your own sounds and combine them into one big symphony with this loop station. The user can record several recordings and combine them. Then the user can swipe these recordings until he has what he wants. A lot of attention is paid at the visualisation, which I will not in this way.
* Geluidsrecorder: this application is standard from android itsself. The user can record his own voice and save this to a file.

#### Hardest parts

* Making a loop. Once recorded one round, it has to be played the next time the loop plays.
* At the end the file has to be saved to a directory in the mobile phone.


## Purpose

The purpose of this app is to loop recorded audio and make covers this way.


## Screenshots

![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58.jpeg)
![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(1).jpeg)
![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(2).jpeg)
![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(3).jpeg)
![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(4).jpeg)
![alt text](https://github.com/suitendaal/finalProject/blob/master/doc/screenshots/WhatsApp%20Image%202018-01-30%20at%2013.44.58%20(5).jpeg)


## Copyright

Sven Uitendaal owns the material in this repository.


## Better Code Hub

[![BCH compliance](https://bettercodehub.com/edge/badge/suitendaal/finalProject?branch=master)](https://bettercodehub.com/)
