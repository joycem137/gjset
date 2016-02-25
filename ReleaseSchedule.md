# Release 0.1 - Single Player #

This will be a rudimentary single player version of the software.

Requirements
  * Player will be able to select cards
  * System will determine if selected cards are a set and react accordingly.
  * When there are no more sets remaining, the program will indicate that the game is over.
  * The player will be able to indicate that they want more cards, as they cannot find a set.

# Release 0.2 - Rudimentary Online Play #

This release will take the basic capabilities of the single player mode and extend them to support multi player capabilities.

Requirements
  * Server
    * Must be client-platform agnostic
    * Must handle deck delivery
    * Must handle identifying that players want more cards.
    * Must handle situation where no sets remain.
    * Must handle player adding and drop outs.  I'm thinking of using game ids to start with.
    * Must handle calling of set.
    * Must maintain player scores.
    * Must validate set selections
  * Client
    * The player's current score will be displayed
    * The number of cards remaining in the deck will be displayed.
    * Player will be able to call set by clicking cards or clicking a "Call set" button.
    * If the player begins clicking, or clicks set, the player will have a limited time to select cards.
    * If the player selects something that is not a set, or does not select cards in time, their score will be penalized.
    * The score change will be indicated graphically.

# Release 0.3 - UX Improvements #

Okay, I'm really not very happy with the UX of the system.  We're going to need to do some major improvements to fix the issues we have before moving on.

Requirements
  * Client
    * System must display splash screen on start up to allow selection of single or multi player.
    * Add animation to the cards.
    * Improve the look of the highlighting
    * Add a mouse-over highlighting.
    * Change the player panels to actually look half-way decent, instead of looking like crap.
    * Use a photorealistic background.
    * Investigate ways of improving the look of the cards themselves.
    * Display all open game ids on the server.

# Release 0.4 - Multi Player enhancements #

This version will enhance the multi-player experience by providing socialization tools.

Requirements
  * Software will support a chat log
  * Software will support user portraits
  * User portraits will be displayed for each player
  * All systems will store a local copy of the score board.
  * Score tracking will display win/loss ratios for each individual player, and each player matchup.
  * Server chat room for pre-game configuration

# Release 0.5 - Graphical enhancements #

This version should be the prettified version of the rest of the system.  This will basically be the second version of the program, tearing down what came before it and replacing it with a whole new skin.

Requirements
  * Software should appear attractive on all points.
  * Software should provide easy-to-use menus and options.

# Release 1.0 - Actual release #

This should be the first fully realized version of the software.

Requirements
  * Software successfully provides users with a Set experience that is similar to playing the game in person.

# Future Ideas #
  * Create a central server for finding players.
  * Add a "speed run" mode for single players.
  * Create a computer player for single player mode.
  * Implement chess-like ratings system.
  * Provide more customization and options for players, such as:
    * colorblind mode
    * handicapping options to better allow different skill levels to play together (i.e. 10-second lockout for experts, smaller penalties for novices, etc)
    * can choose to hide scores until the end of game (similar to playing with actual cards)