-- growlNotifier.applescript
--
-- This script is used for sending message/notifications via Growl.
-- It is really easy to use, and there is no need to to install something else apart
-- from Growl itself (no need to have growlnotify or such stuffs ... !) 
-- If Growl is not installed, then an alert box is shown with the message to 
-- display  !
--
-- Usage: osascript growlNotifier.applescript <AppName> <Message> [WindowTitle]
--
-- Author: Papa Issa DIAKHATE (paissad) <paissad@gmail.com>
-- License: GPLv3
--
-- ===============================================

global appName, appIcon, allNotificationsList, enabledNotifications
global mainNote, errorNote
global windowTitle, errMsg

on run args
	tell application "System Events" to set isGrowlRunning to exists application process "GrowlHelperApp"
	
	set appName to item 1 of args
	set appIcon to appName
	if ((count of args) > 2) then
		set windowTitle to item 3 of args
	else
		set windowTitle to appName
	end if
	set mainNote to "mainNote"
	set errorNote to "ErrorNote"
	set enabledNotifications to {mainNote}
	set allNotificationsList to {mainNote, errorNote}
	
	set msg to item 2 of args
	set errMsg to "(is Growl running or installed ?)"
	
	NotifyMe(msg, isGrowlRunning)
end run

-- The Handler
to NotifyMe(Message, Growler)
	if Growler then
		tell application "GrowlHelperApp"
			register as application appName all notifications allNotificationsList default notifications allNotificationsList icon of application appIcon
			
			notify with name mainNote title windowTitle description Message application name appName
		end tell
	else
		tell application "System Events"
			set alertMsg to Message
			display alert windowTitle & " " & errMsg message alertMsg giving up after 10 as warning
		end tell
	end if
end NotifyMe
