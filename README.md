<h1>Music Player App using Django Rest and Android Java</h1>

Background: This was a semester project for an Android development course I took at university. Around the same time, I was learning the Django rest framework, so I thought why not integrate both into a single app.

<h3>Django Api</h3>
The API looks like

![image](https://github.com/IbrahimBM2714/music-player-app-using-django-rest-and-android-java/assets/115867055/cd41683b-774d-4737-9984-4dd4ad1877d2)
<br>
The API stores the songs in the form of a file link. This is because I knew that in Android I could easily get those songs if they were in the form of a link. 
Both the API and the device your Android app is running on should be connected to the same network. This is because the API will be hosted locally and only the devices connected in that local network will be able to access it.

These are the steps to run the API:
<ul>
  <li>
    First, install Django and Django rest framework. This can be done by running pip install Django and pip install Django rest framework
  </li>
  <li>
    In the terminal, write ipconfig and hit enter. Now note down the IPv4 address. After which go into MusicPlayer -> musicAPIDjangoRest -> musicPlayer -> settings.py and paste that ipv4 address into ALLOWED_HOSTS. After which, type python manage.py runserver [ipv4 address]:[any port number] and hit enter. This will launch the API on your browser.
  </li>
</ul>

<h3>Android APP</h3>
Just because the API is hosted locally, the API will have a different URL for everyone. This warrants a small change in the Android code too.
In Line 46 of SongDisplay.java, change the apiUrl to the URL of the API as seen in your browser. That is all the change required. After that run the app on either an emulator or your device.
<br>
This is what the app looks like:
App Icon
<br>

![image](https://github.com/IbrahimBM2714/music-player-app-using-django-rest-and-android-java/assets/115867055/45944dd0-efaa-4148-a20b-40c58a13d1d5)

<br>

Songs Display
<br>

![image](https://github.com/IbrahimBM2714/music-player-app-using-django-rest-and-android-java/assets/115867055/8622e39d-16bf-4306-a0d5-bafeb5d0017b)

<br>

Song Playing
<br>

![image](https://github.com/IbrahimBM2714/music-player-app-using-django-rest-and-android-java/assets/115867055/f7951df5-ff40-46c8-b9ec-57c9ecbc8a8b)

<br>
