# Gallery Vault Application

## Table of Contents
* [Running the code](#running-the-code)
* [Features](#features)


## Running the code
1. Git clone this repository or download the files as .zip
2. (For zip files) Extract the files
3. Open Android Studio (or IntelliJ) and click "Open"
4. Choose the folder of the app
5. Connect your Android phone to the computer and make sure to enable USB debugging
6. Click run and choose the connected phone to run the app
7. App should install and automatically open on the phone

Another option : Download the .apk file [here](https://drive.google.com/file/d/1a9foKaoBaCNEp0vPTsDoVtGT2BaaoF83/view?usp=sharing) from your phone and click open and it will automatically install the app.

## Features
### 1. Calculator front

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/calculator.JPG)

Note : To enter the app, press "1234" and then "="

### 2. Password lock to protect your data

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/passcode.JPG)

Note : Default password is "1234" and users are encouraged to immediately change the password.

### 3. Stored password is hashed
See LoginFragment and AlbumsFragment for implementation

### 4. Create Folders

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/create_album.JPG)

### 5. Add multiple Images and Videos

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/add_images_videos.JPG)

Note : For Samsung Gallery I don't think it supports multiple images/videos so for now if the user choose Samsung Gallery as the source, the user can only pick 1 image.

### 6. Change the password

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/change_password.JPG)

### 7. Preview Images and Videos

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/preview_videos.JPG)

### 8. Take the perpetrator's picture using front camera after 3 failed tries

![img](https://github.com/sesiliafenina/gallery-vault/blob/images/front_capture.JPG)

Note : The front capture looks like this because it was taken from an emulator. If you were to download this app and run it, it will take a picture using the front camera.
