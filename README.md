Blue Answer
=========

Blue Answer is a portable Personal Response System (PRS) written in J2SE & J2ME to faciliate and gather audience feedback. Developed as part of a project proposing that ubiquitous mobile phone technology found within the lecture theatre could replace proprietary handsets, often sold at large cost as part of commercial solutions, targeted towards Higher Education institutions.

__Disclaimer: This project has not been maintained since April 2009 and, while fully functional, has since been outpaced by mobile technology advancements. Please see both the Requirements and Hardware Compatibility sections for a clearer idea of what is supported.__

Features
-

* Send multiple choice questions to participant's mobile phones using Bluetooth.
* View audience responses in real-time as a histogram, export results as a PNG image.
* Support for pre-prepared quizzes (written in XML format).

Requirements
-

* A workstation with the Java Runtime Environment (5.0+) and a Bluetooth dongle plus drivers (e.g. WIDCOMM) installed. Tested under Windows XP/Vista (Unix/Mac OSX untested).
* Bluetooth-enabled mobile devices that support Java MIDlets (CLDC 1.1 and MIDP 2.0).

Hardware Compatibility & Limitations
-

In theory, any mobile device that follow the Connected Limited Device Configuration (CLDC) 1.1 and Mobile Information Device Profile (MIDP) 2.0 specifications can run the client application. In practice, based on compatibility testing in the wild, implementations of the Java VM are varied across devices, resulting in performance issues and other bugs. Some devices may not be able to connect to the server due to poor implementations of the JSR 82 Bluetooth API.

The true limit of the number of devices that can connect to the server has not been tested. For any set of interacting Bluetooth devices, there is a single 'master' device that controls the timing of connections between remaining 'slaves'. This configuration is called a 'piconet', and the Bluetooth specification is explicit when stating that only 7 slaves can be active in any one piconet. However, it goes on to state that "many more slaves can remain connected in a parked state" (Bluetooth SIG, 2009), though it does not specify a number (most observers believe it to be 255). My own testing has demostrated 9 connected devices all receiving questions and sending responses successfully but results may vary.

Installation
-
Packaged archives of the client and server software can be found in the *dist* folder. The client application files are __BlueAnswerClient.jar__ and __BlueAnswerClient.jad__, the server application is __BlueAnswerServer.jar__ with an example quiz file called __quiz.xml__.
    
### Server 

The server application requires no installation, you may place the JAR file where you feel most comfortable running it.

### Client

The client application must first be transferred to each mobile device, with possible further installation steps to follow, depending on the destination’s make and model and directed by the device’s interface.

There are two ways to successfully deliver the client application to a device:

1. Follow your manufacturer’s guidelines and advice on how to transfer applications via data cable, infra-red or other means (you may be required to use special software).
2. Use the Bluetooth software installed on the workstation to pair with the device and send the JAR file.

Once the client JAR file has been successfully transferred, your device's security settings may require further confirmation that you wish to install the application.

Usage
-

1. Run the server application.
2. Run the client application on each mobile device (connecting to the server is an automatic process).
3. Write a multiple choice question and click "Send question" when your participants are ready (consult the device list to the left, at least one client must be connected).
4. View audience responses by clicking "Show results now" (automatically shown after 60 seconds).
5. Save responses by right-clicking on the results window.

###Client Instructions

* Depending on your device's security settings you may have to authorise the Bluetooth connection when prompted.
* When a question has been received, it is displayed in a scrollable dialog box.
* Question dialogs are hidden using the __Hide__ soft key and only then are the available answers are displayed. These can be selected by either navigation key on your phone, or the number keypad, where 1 = A, 2 = B etc.
* If the answer text is longer than the length of characters displayable on the device's screen, then it will scroll across; scrolling can be paused by pressing and holding either the __star (*)__ or __hash (#)__ key on the device.
* The question can be viewed again at any time by pressing the __View Q__ soft key.
* To send the selected response, press the __middle navigation__ key. Once the response has been sent, the client will wait for another question. There is also the ability to not answer a question by pressing the __Skip__ soft key.

###Preparing A Quiz

A sample XML file is provided to illustrate the correct structure that must be used when creating a quiz. The following block of code illustrates a well-formed quiz document:

    <?xml version="1.0" encoding="UTF-8"?>
    <quiz>
        <!-- the quiz number -->
        <quizNumber>1</quizNumber>
        <!-- title of the quiz -->
        <quizTitle>My Quiz</quizTitle>
        <questions>
            <!-- each quiz can have any number of questions -->
            <question>
                <!-- the question number -->
                <questNumber>1</questNumber>
                <!-- the question -->
                <questText>What colour is the sky?</questText>
                <answers>
                    <!-- each question can have up to 4 answers -->
                    <answer>
                        <!-- the answer number -->
                        <ansNumber>1</ansNumber>
                        <!-- the answer -->
                        <ansText>Blue</ansText>
                    </answer>
                    <!-- value of ansNumber that is the correct answer -->
                    <correctAnsNumber>1</correctAnsNumber>
                </answers>
            </question>
        </questions>
    </quiz>

Once your quiz has been prepared and saved with the .xml extension, it can be loaded into the server application by selecting __File__, __Open quiz...__ (note: changes made to a particular question in the server application will not be preserved.)