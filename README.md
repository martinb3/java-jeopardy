java-jeopardy
=============

From the original http://sourceforge.net/projects/dlauritz-javjeo/.

## Introduction

Thank you for using this program!

My Java version of the popular Jeopardy game is meant for classroom-style use. One moderator runs the program, selecting the questions and managing the score (using the Score Card tool) while an external monitor or projector displays the screen to students.

## Java

To run the Jeopardy program, Java must be installed. If your computer is not equipped with Java, the latest JRE can be downloaded at <http://java.com/en/download/index.jsp> . 

With Java installed, you should be able to simply execute the .jar file like a normal program.

## Running the Game

The main Jeopardy window simply displays a Jeopardy board and several menu options.

#### The Menu

*   Game 
    *   *Load New Game (Ctrl+O)*: Load a new game
    *   *Exit (Ctrl+Q)*: Exit the program
*   Round 
    *   *Show Single Jeopardy (Ctrl+S)*: Show the Single Jeopardy round.
    *   *Show Double Jeopardy (Ctrl+D)*: Show the Double Jeopardy round.
    *   *Show Final Jeopardy (Ctrl+F)*: Show the Final Jeopardy screen.
    <blockquote style="color: red;">
      Warning: Selecting one of these menu options completely resets the screen. If questions from a round have already been selected, they will be reset.
    </blockquote>

*   Tools (See Tools section for details) 
    *   *Score Card (Ctrl+C)*: Open a score card window
    *   *Game Builder (Ctrl+B)*: Open the game builder program

## Files

This program uses YAML-formatted text files to store game data. The best way to ensure useability and accuracy is to use the built-in Game Builder tool (see the Tools section) to edit or create new games that can be played in the main frame.

The following files are required to use this program: 
*   A .yaml game data file
*   EITHER 
    *   **Jeopardy_full.jar** - contains all program data
*   OR 
    *   **Jeopardy.jar** - contains game data
    *   **jyaml.jar** - Contains YAML library for file IO
    *   **builder.jar** - The game builder tool as a standalone program. 
    *   **scorecard.jar** - The score card as a standalone program.

Jeopardy\_full.jar is larger because it contains all program data. In contrast, the smaller individual JAR files each only contain the required code for their portion of the program. If you are not using Jeopardy\_full.jar, \*all\* four files are required. I recommend the smaller files, because then individual programs can be accessed without opening the main program.

The following files are for your information: 
*   **README.html**/**README.txt** - This file, in HTML and TXT formats.
*   **GNU_GPL.html** - The GNU General Public License, under which this program is provided.

Other files that may be distributed with this program: 
*   **empty.yaml** - An example of a game. No interesting information, but can be used to see how the program runs.
*   **doc/** - The JavaDoc information. Likely only interesting to programmers.
*   **src/** - The Java source files. Also useful only for programmers. 

## Tools

There are two "Tools" accessible through the Tools menu within the main Jeopardy window.

#### Score Card

This tool is a standalone program for managing the scores of a group of players. Since the Jeopardy game itself only displays questions, players' score must be managed with this tool. Start with an empty list or load a .yaml file containing previously saved data. Add, edit, or remove players with the buttons, and add to their score with the "Change Score" button.

Player score data may be saved between sessions of game play by selecting "Save List" from the File menu. Similary, player data is loaded from the "Load List" menu item and list data is reset by selecting the "New List" item. If you do not wish to save data, select "No" when prompted to save.

#### Game Builder

This tool is the official mechanism for editing and creating games for this program. 
*   Select single or double jeopardy with the program menu.
*   Edit categories by clicking their names.
*   Edit entries by clicking on the corresponding square and entering the new name in the dialog.
*   Hover over a grid square to get a preview of the answer and question as it will be displayed in-game.
*   Text in the answer and question fields can be plain text or HTML-formatted data. Tables and images will display in-game. Other non-text elements are not guaranteed. After a game is saved in the game builder, it can be loaded into the main Jeopardy program to test or play. 

## Copyright and Use

#### Author

This program was written by Dallin Lauritzen.   
EMail: dallin.lauritzen@gmail.com   
The most recent version can be found at <http://dallin.lauritzenfamily.net/jeopardy> 

#### License

This program is licensed under the GNU General Public License, included with this program or found online at <http://www.gnu.org/licenses/gpl-3.0-standalone.html> . 

#### Use

If you wish to use this program, for personal or educational purposes, please email me to let me know that my work is appreciated. Feedback is welcome and desired. If you give me your email address, I will email you if and when new versions of the program are complete. 

#### Libraries

JYaml library can be found at <http://jyaml.sourceforge.net/index.html> .