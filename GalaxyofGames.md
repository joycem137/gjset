# The present #

The purpose of gjset, since the beginning, has been to be a programming project for a small group of people that want to do some coding on their own time but do not have a great deal of time to dedicate to a project.  Minimal progress is made over time, but corner are not cut.  This is intended to be a professional quality execution of a software project.  Our prioriters are as follows:

## Our priorities ##
  1. Develop professional quality code, using good design, architecture, documentation, etc.
  1. Design an interface that is visually attractive and easy to use, and not just a cluster of buttons thrown on the screen at random.
  1. Practice using various programming practices, such as Extreme Programming's "No early functionality" and "Simple Design" practices.

# The Future #
gjset may only be the beginning of this team's efforts.  If we are able to put in sufficient time to complete gjset, the next project will tie into gjset in such a way as to start moving towards a sort of "galaxy of games" that we could work on.

There are many gaming portals available on the internet.  However, these gaming portals suffer from a variety of issues that detract from the quality of their games.  Some sites, like Yahoo! Games, have an extremely large variety of games, but have an absolutely poor interface for accessing them.  Additionally, you can only play these games through your web browser, and do not have the choice of deciding what interface you want to use.

Other portals, such as Richard's PbEM Server, allow play-by-post type play, but do not offer a quality interface for interacting with the game.  Everything is done through email.  Some web clients and other clients exist, but I have not yet found many implementations, and most suffer from being a laggy, functional-but-not-pretty interface.  Its-your-turn.com seems to have a similar issue.

Others do one thing and do it really well, as is the case with IGS, the Go server.  The open server protocol and popularity of the server has resulted in many different implementations of the interface for a variety of platforms, all of which can interact with one another.  And these clients can be quite well done.

This "Galaxy of games" concept will be directed to solve the problems of these past games.

## Objectives ##
  1. Users will be able to use a variety of interfaces to access both the game portal and the games themselves.  The protocol will be completely open for alternate implementations.
  1. Each game will be designed to best approximate the experience of playing the game in person, such as in the case of gjset.
  1. Players will be able to play games in real-time, and in the case of turn-based games, play at user defined intervals.
  1. Games will be designed to look great, both in UI and in visual aesthetics.

# Architecture #
Matching server will have associated front end software that launches the individual games when a new game is spawned.

When a game launches, the server will spawn a new thread to host that game.  The client will launch the game logic and the front end will go to sleep until notified by the game program that the game is complete.