# AdvancedTopics-Week5
## Major Info (TLDR)

### Goals:
The goals for this midterm were to create a music player app. One which scanned your entire phone for certain types of files, in this case .mp3 .ogg and .m4a. The collected files path's will be saved and then displayed in a listView. The items in the listView can then be clicked to be played in a new activity.

### Problems Encountered

#### Problem 1: Permissions and MinAPI
##### Status: Solved
To read and write to/from external storage in Android is always sort of a pain, mainly due to the permissions required and the methods that can be used to get a dynamic filepath. The methods and ways to access external storage directory seems to change all the time, and finding what works with what API can be a challenge. For example, the method getExternalStorageDirectoryPath would have worked great for what I was doing, but is only available for a minAPI of 30, which is Android 11, and my phone is Android 10. This was solved by just hardcoding a starting directory, and then recursively checking all child files and directories of the starting directory.

### App Completion Status 85%
This App is completed and functions properly, but is sometimes buggy and I would like to add new features such as being able to organize the listView into seperate playlists.

### Tools and Resources
#### Video Tutorial
https://www.youtube.com/watch?v=74BMukDxEr8
#### Stack Overflow
https://stackoverflow.com/questions/5280176/make-directory-in-android
#### Dexter
https://github.com/Karumi/Dexter
### Visualizer
https://github.com/gauravk95/audio-visualizer-android

## Beta Version APK
You can find the app for yourself here:
https://drive.google.com/file/d/1SGcSNefRKQnQBaMN8gkEovjgIipflb38/view?usp=sharing
