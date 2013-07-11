GW2 Event Notifier
==================

This application creates notification for upcoming in-game events in form of popups (like Steam messages, nothing game blocking).

![GW2 Event Notifier notifications](http://pakldev.de/wp-content/uploads/2013/06/gw2evno_screen2.png)

It is necessary that you play Guild Wars 2 windowed or in "Windowed Fullscreen" mode.

Furthermore are you required to change the map manually.<br />
In-game you can just press CTRL+Backspace. A dialog will pop up to easily search a map by it's name.

The Guild Wars 2 API may allow to find your position in the future. Until then there is no other way.

For information about, well..., everything make sure to [visit the wiki](https://github.com/PakL/GW2EventNotifier/wiki)!
***
**[Get the latest downloads here.](https://github.com/PakL/GW2EventNotifier/releases/)**
(There's a changelog, too!)

Tested only on Windows 7 64bit w/ Java 1.7.0.<br />
But should also work with Java 1.6.x and/or 32bit system. Don't know about MacOS or Linux, though.
***
This application uses the official [Guild Wars 2 API](https://forum-en.guildwars2.com/forum/community/api/API-Documentation) and isn't even near complete.
***
Dependencies:
* [json-simple](https://code.google.com/p/json-simple/)
* [JKeyMaster](https://github.com/tulskiy/jkeymaster)
	+ [Java Native Access](https://github.com/twall/jna)
* [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)