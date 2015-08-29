Craft (Support) Email Intents
=============================

An Android library that enables developers to create email intents easily. Also includes feature to retrieve phone's details for debugging purposes.

<b>Why?</b><br>
I needed a nice abstraction for an Android app developer like myself to create and launch email intents from their app. Seeing as how no such abstraction exists, I decided to write one and open source this solution. This library also supports retrieving the phone's hardware/software details and include them in the email.

<b>What can you do?</b><br>
Before you begin playing with the JAR library, I suggest that you install the APK from the releases section and experiment. This tiny Android program wraps the library and shows what you can do with this library.

![Screen](/screenshots/details.png)

Press the buttons to obtain what contents you want in the email


![Screen](/screenshots/enter-email-details.png)

Press the buttons to obtain what contents you want in the email. Press "Email This" to send generate and send the intent.


![Screen](/screenshots/enter-application.png)

Intent is received by the email application with the supplied details.



Attachment is included as well. (Only working for the Gmail app. The normal Email app does not process attachments)


<b>Getting Started.</b><br>
0. Download and drop the jar file into the libs folder of your Android project<br>

<b>To just craft a basic email intent and send it:</b>

1. Instantiate the class CraftIntentEmail. 3 constructors are available but let's use the simplest with no parameter, CraftIntentEmail().<br>

2. Construct the email my adding information like To, CC, BCC, Subject and Content using the relevant public methods, add...(), remove...() and append...(). All methods will throw IllegalArgumentException if you pass null, empty or invalid emails as parameters. If you wish to change this behavoir, override the protected methods isFieldValid(String field) and isEmailValid(String emailStr).<br>

3. This library only supports one attachment at the moment. Add an attachment by passing the file path or URI path to addAttachment(). According to my tests, this only works for the Gmail app and not the typical Email app. Calling addAttachment again will replace the path.<br>

4. Retrieve the Email Intent with the method generateEmailIntent(). Your can also use generateEmailIntentWithNewTaskFlag() where the intent will tell Android that when the user returns to your app, your app is displayed, instead of the email app. Modify this intent if you have to then send it with sendIntent(Context context, Intent intent).<br>


eg
```
CraftSupportEmail emailGen = new CraftSupportEmail(context, email, subject);
emailGen.appendContent(myContent);
Intent intent = emailGen.generateIntentWithNewTaskFlag();
boolean status = emailGen.sendIntent(context, intent);
```
"status" returns true if there is an Email app available to receive the intent on the phone.

<b>Retrieve and use the hardware/software detail dumps</b><br>

1. Follow steps 1 and 2 in "Getting Started", except instantiate the class CraftSupportEmail instead.<br>

2. Extra methods are available like getBasicDetails(), getMinimalDetails() and appendAllDetails(). To see what this methods do, play with the APK provided.<br>

3. Follow Step 3 in Getting Started.<br>


<b>Retrieve phone's details</b><br>
Instantiate the GetInfo...() classes directly. Play with public methods.


The MIT License (MIT)<br>
Copyright (c) 2013-2014 Yeo Kheng Meng<br>
