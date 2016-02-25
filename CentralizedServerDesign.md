# Centralized Server Design #

## Connection Overview ##

  1. Application Start
  1. **Selection**: Singleplayer or Multiplayer
    * **Selected** Multiplayer
  1. **Selection**: New User or Username/password
    * **Selected** New User
  1. **Data Entry** Username and password
    * If username is taken, user is prompted to re-enter information.
  1. **Selection**: New User or Username/password
    * **Selected** Username and Password
  1. **Selection** Lobby Screen
    * Lobby screen should, minimally, have a list of "tables" that players can sit down at to play
    * Only tables with open seats are seen here.  Other tables are hidden.
  1. **Selected** Player selects a table.
    * Information about the table appears, including who is playing, any rules, etc.
    * **Option** Join table
  1. **Selected** Join Table -> Table Screen
    * **Option** Ready to Play
    * List of players
  1. **Selected** Ready to play
    * When all players have done this, the game starts.
  1. **Game Complete**
    * Return to the table, not the lobby
    * Players may start another game.


## Other scenarios ##

### Scenario 1: Disconnection ###
  1. User unintentionally disconnects from an active game.
  1. Table holds a place for the player
  1. User reconnects
  1. User is given the option to reconnect to the game in-progress

## Future Features ##
  * Lobby and table chat
  * In game chat
  * Direct messages
  * Password protected tables
  * Email address protected usernames