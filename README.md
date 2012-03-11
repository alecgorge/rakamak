# Rakamak

Rakamak is a plugin to secure a server, even when `online-mode` is set to `false`.

This plugin was original authors by Erwyn and Dorpaxio, but they have long since abandoned the project (last update was on 8/12/2011).

I (alecgorge) have updated this plugin to work with 1.1-r6 and 1.2-R0.2 and hopefully will add some more features as requested.

## Download

You can find the latest downloads [on Jenkins](http://ci.alecgorge.com/job/Rakamak/).

Original about:

> The Rakamak plugin has been created to provide a security opportunity for
> servers with online-mode=false.
> 
> Cause I was in a particular network configurations with players accessing the
> server by the Internet and others by the local Network without Internet access
> I was encoutering lots of account stealing, particularly adminisatrators accounts
> (oh? isn't it?).
> 
> The Rakamak plugin is a simple plugin which asks a password previously set up
> by the player, before being able to do anything with his character.
> 
> It's my first plugin, so I probably have lots of things to learn, but it works...
> 
> ## Features:
> 
> * Set a password for each player
> * Protect players from account stealing by asking it
> * Disable all commands but the login's one when not logged.
> * Possibility of changing password > /changepass <currentpassword> <newpassword>
> * The player can move only in a radius of 5 blocks.
> * Later login, the gamemode of player is changed automatically in survival.
> * Disable the chat if player isn't login ( Optional In config )
> * A player op. It isn't op later login.
> * Possibility of changing the radius of the not movement ( Config )
> * Possibility of changing the text.
> * No damage if player is not logged.
> * Kick player if he tried too many password.
> 
> ##Usage:
> 
> * /register <thepasswordyouwant> to protect your character
> * /login <thesamepassword> to authenticate on login
> * /changepass <currentpassword> <newpassword> to change your password
> 
> ## TODO:
> 
> * Permission support
> * MySQL support
> * The MySQL support will be optional ( Config )
> * Fix bug with WorldEdit ( & Essential )
> * Fix bug of Command_Preprocess after a /reload.
> * When there is a update, the settings and templates files are automatically updated!

