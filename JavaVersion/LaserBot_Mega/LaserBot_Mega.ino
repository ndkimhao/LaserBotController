#include <string.h>
#include <UTFT.h>
#include <UTouch.h>
#include "digitalIOPerformance.h"
UTFT TFT;
UTouch Touch;
extern uint8_t SmallFont[];
extern uint8_t BigFont[];
extern uint8_t BiggerFont[];

const byte START_SCREEN = 0;
const byte WAITFILE_SCREEN = 1;
const byte SETTING_SCREEN = 2;
const byte RUN_SCREEN = 3;
byte curScreen = 0xFF;
void switchScreen(byte screen);
#include "MISC.h"
#include "Command.h"
#include "Button.h"
#include "ProccessBar.h"
#include "RunScreen.h"
#include "WaitFileScreen.h"
#include "StartScreen.h"
#include "SettingScreen.h"

void switchScreen(byte screen) {
  curScreen = screen;
  TFT.setBackColor(TRANSPARENT);
  switch(screen) {
  case START_SCREEN:
    StartScreen_init();
    StartScreen_draw();
    break;
  case WAITFILE_SCREEN:
    WaitFileScreen_draw();
    break;
  case SETTING_SCREEN:
    SettingScreen_init();
    SettingScreen_draw();
    break;
  case RUN_SCREEN:
    RunScreen_init();
    RunScreen_draw();
    break;
  }
}

void loop()
{
  if (commandState == COMMAD_STATE_NORMAL) {
    Command_process();
  }
  else if(commandState == COMMAD_STATE_COLOR_IMAGE) {
    Command_processColorImage();
  }
  else {
    Command_processBWImage();
  }
  if(isCompleteCommand && command[0] == COMMAND_HOME) {
    isCompleteCommand = false;
    switchScreen(START_SCREEN);
  }
  switch(curScreen) {
  case START_SCREEN:
    StartScreen_process();
    break;
  case WAITFILE_SCREEN:
    WaitFileScreen_proccess();
    break;
  case SETTING_SCREEN:
    SettingScreen_proccess();
    break;
  case RUN_SCREEN:
    RunScreen_proccess();
    break;
  }
  if(digitalRead(53) == LOW) {
    Command_send(COMMAND_SHUTDOWN);
    shutdownScreen_draw();
    delay(15000);
  }
}

void setup()
{
  TFT.InitLCD();
  TFT.fillScr(WHITE);
  Touch.InitTouch();
  Touch.setPrecision(PREC_MEDIUM);
  //Serial.begin(115200); // Console DEBUG
  Serial1.begin(115200);
  startupScreen_draw();
  Command_send(COMMAND_RESTART);
  pinMode(53, INPUT_PULLUP);
}
