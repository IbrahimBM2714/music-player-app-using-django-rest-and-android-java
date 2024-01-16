<h1>Music Player App using Django Rest and Android Java</h1>

Background: This was a semester project for an android developement course I took in university. Around the same time, I was learning the django rest framework, so I thought why not integrate both into a single app.

<h3>Django Api</h3>
The API looks like

![image](https://github.com/IbrahimBM2714/music-player-app-using-django-rest-and-android-java/assets/115867055/cd41683b-774d-4737-9984-4dd4ad1877d2)
<br>
The API stores the songs in the form of a file link. This is because, I knew that in android I could easily get those songs if they were in the form of a link. 
Both the API and the the device your android app is running on should be connected in the same network. This is because, the API will be hosted locally and only the devices connected in that local network will be able to access it.

These are the steps to run the API:
<ul>
  <li>
    First, install install django and djangorestframework. This can be done by running pip install django and pip install djangorestframework
  </li>
  <li>
    In the terminal, write ipconfig and hit enter and note down the ipv4 address. After which go into MusicPlayer -> musicAPIDjangoRest -> musicPlayer -> settings.py and paste that ipv4 address into ALLOWED_HOSTS. After which, type python manage.py runserver [ipv4 address]:[any port number] and hit enter. This will launch the API on your browser.
  </li>
</ul>
