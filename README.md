# DiscordConnection
A Spigot plugin that connects Discord bot with Minecraft server
# Features
* Chat between Discord and Minecraft 
* Reply Discord message from Minecraft
* Broadcast death message + death count
* Broadcast advancement message
* Execute Minecraft command from Discord channel
* Get server ip by command in Discord channel
* Display playercount on activity status

# How to Use
### For Players
* Chat in Minecraft, and your message will be sent to Discord
* If you want to reply to a message, just click on it 
* In Discord Channel, you can type `-ip` to get the server IP `-list` to get online players
### For Administrator 
* Setup the plugin by the installation below
* type any commands in the console channel to execute it in Minecraft server
# Installation
### Create a Discord Bot and Add it to Your Discord Server
1. Go to https://discord.com/developers/applications
2. Click **New Application** to create a new application ![Web capture_5-3-2022_082_discord com](https://user-images.githubusercontent.com/69295512/156802701-fcc81ea5-ae84-4366-b7cc-7381d8ddef72.jpeg)

3. Go to **Bot** tab and click **Add Bot**![Web capture_5-3-2022_0115_discord com](https://user-images.githubusercontent.com/69295512/156803209-89b9c832-6eab-426f-a2b1-92a74a70fe80.jpeg)



4. Copy the Bot's Token and save it for later use ![Web capture_5-3-2022_01122_discord com](https://user-images.githubusercontent.com/69295512/156803566-cd7c4438-b4f9-4329-89a2-d070ff9c48c4.jpeg)
5. Go to **OAuth2/URL Generator** tab and check **SCOPES/bot** and **BOT PERMISSIONS/Administrator** and copy the url![Web capture_5-3-2022_01219_discord com](https://user-images.githubusercontent.com/69295512/156804694-99403211-905d-4e1c-891e-6c72e6713d59.jpeg)


6. Paste the url in your browser and add the bot to your server 
### Minecraft Server Set Up
1. Go in to your minecraft server 
2. Use `/discord token <token>` put in the token you just copied
3. Go to your Discord Server and right click the channel you want to connect and copy the id (If you don't see copy id in your menu, go to User Settings/Advanced and turn on Developer Mode)
4. Go back to the Minecraft server and use `/discord channel chat set <id>`
5. Beside chat channel,you can also set other channels by using `/discord channel <channel> set <id>` 
   - available channels are:
   - `chat`:use for chat between Minecraft and Discord
   - `death`:use for broadcast death message
   - `achievement`:use for broadcast advancement
   - `console`:use for execute minecraft command from discord
   - `all`:set all the channels 
6. If you want to clear any channel, just use `/discord channel <channel> clear`


# Commands 
## minecraft

### for everyone:
- `/reply <messageid> <text>` -- reply to the message
- `/mention <userid> (<text>)` -- mention a user 

### op required:
- `/discord token <token>` -- set the bot's token
- `/discord channel <channel> <method> (<id>)` -- set the Discord channel connected to the bot
- `/discord activitytext <text>` -- set the text you want to display on the bot's activity status
- `/discord deathcounttext <text>` -- set the DeathCount display text that shows when a player died

## Discord

- `-ip` -- get Minecraft server's public ip
- `-list` -- get online players 
