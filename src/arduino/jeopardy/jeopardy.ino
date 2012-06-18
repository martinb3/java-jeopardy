//
// This sketch implements a "Jeopardy" style contestant response tiebreaker.
//
// Each contestant has a pushbutton switch. When they know the answer,
// they push it.  This turns on the corresponding light.
// The light for the first person to push their button blinks.
//
// Pressing the reset switch restarts the game.
//
// J. Peterson, Apr '12
//

// The LEDs, cathodes connected to ground
int Lights[6] = {2, 4, 6, 8, 10, A2};

// The pushbutton switches connect the input to ground when closed.
int Switches[6] = {3, 5, 7, 9, 11, A3};

// Connect to ground to reset the game
int Reset = A1;

// Set to -1 when no winner set.
int winner;

boolean SwitchPushed[6] = { false, false, false, false, false, false };

void setup()
{
  
  Serial.begin(9600);  // initialize serial:
  
  int i;
  for (i = 0; i < 6; i++)
  {
    pinMode(Lights[i], OUTPUT);

    pinMode(Switches[i], INPUT);
    digitalWrite(Switches[i], HIGH);  // Enable pullup on digital input
  }

  pinMode( Reset, INPUT );
  digitalWrite( Reset, HIGH );    // Enable pullup on analog input

  LightsOff();
}

void loop()
{
  int i;

  // If no winner selected, first button selects the winner.
  if (winner == -1)
  {
    for (i = 0; i < 6; i++)
      if (digitalRead( Switches[i] ) == LOW) {
        winner = i;
        Serial.println(winner);
        return;
      }
  }
  
     for (i = 0; i < 6; i++)
     {
       if (i == winner)  // Blink winner's light
       {
         if (((millis() /100) % 10) & 1)
           digitalWrite( Lights[winner], HIGH );
          else
            digitalWrite( Lights[winner], LOW );
       }
       else
       {
         // When switch is pushed, record it.
         if (digitalRead( Switches[i] ) == LOW)
           SwitchPushed[i] = true;

         // Display non-winner lights
        digitalWrite( Lights[i], SwitchPushed[i] ? HIGH : LOW );
       }
     }
  

  // Reset button
  if (digitalRead( Reset ) == LOW) {
    delay(1*1000);
    LightsOff();
  }
}

// Reset to the initial state.
void LightsOff()
{
  Serial.println(-1);
  winner = -1;
  for (int i = 0; i < 6; i++)
  {
    digitalWrite( Lights[i], LOW);
    SwitchPushed[i] = false;
  }
}
