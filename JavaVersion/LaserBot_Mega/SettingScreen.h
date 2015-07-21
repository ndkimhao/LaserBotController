const byte SIZE_ADD_BUTTON = 0;
const byte SIZE_SUB_BUTTON = 1;
const byte DELAY_X_ADD_BUTTON = 2;
const byte DELAY_X_SUB_BUTTON = 3;
const byte DELAY_Y_ADD_BUTTON = 4;
const byte DELAY_Y_SUB_BUTTON = 5;
const byte QUAN_LOW_BUTTON = 6;
const byte QUAN_MED_BUTTON = 7;
const byte QUAN_HIGH_BUTTON = 8;
const byte RUN_BUTTON = 9;
const byte EXIT_BUTTON = 10;

void SettingScreen_init() {
  Button_clear();

  Button_addNew("( + )", 5, 44, BUTTON_AUTO, 64, WHITE, RED, BLUE); // SIZE_ADD_BUTTON
  Button_addNew("( - )", 5, 44+64+16, BUTTON_AUTO, 64, WHITE, RED, BLUE); // SIZE_SUB_BUTTON

  Button_addNew("+", 116, 44, 48, 32, WHITE, RED, BLUE); // DELAY_X_ADD_BUTTON
  Button_addNew("-", 128+32+8, 44, 48, 32, WHITE, RED, BLUE); // DELAY_X_SUB_BUTTON

  Button_addNew("+", 116, 80+24, 48, 32, WHITE, RED, BLUE); // DELAY_Y_ADD_BUTTON
  Button_addNew("-", 128+32+8, 80+24, 48, 32, WHITE, RED, BLUE); // DELAY_Y_SUB_BUTTON

  Button_addNew("Low", 224, 44, 80, 32, WHITE, RED, BLUE); // QUAN_LOW_BUTTON
  Button_addNew("Med", 224, 44+32+8, 80, 32, WHITE, RED, BLUE); // QUAN_MED_BUTTON
  Button_addNew("High", 224, 44+32+8+32+8, 80, 32, WHITE, RED, BLUE); // QUAN_HIGH_BUTTON

  Button_addNew("RUN", 116, 124+80, 180, 32, YELLOW, BLUE, WHITE); // QUAN_HIGH_BUTTON
  Button_addNew("EXIT", 116, 124+40, BUTTON_AUTO, 32, WHITE, BLACK, WHITE); // EXIT_BUTTON
}

void SettingScreen_draw() {
  TFT.fillScr(LIME);

  TFT.setFont(BigFont);
  TFT.setColor(RED);
  TFT.setBackColor(YELLOW);
  TFT.print("Size", 16, 16);
  TFT.print("Delay", 128, 16);
  TFT.print("Qual.", 224, 16);

  TFT.setBackColor(TRANSPARENT);
  TFT.print("X=", 112, 80);
  TFT.print("Y=", 112, 80+24+36);

  TFT.setColor(RED);
  TFT.print("W=", 5, 200);
  TFT.print("H=", 5, 200+16);

  Button_draw();
}

void SettingScreen_proccess() {
  Button_proccess();
  static boolean prevSizeButtonState = false;
  static char prevButton = -1;
  char pressedButton = Button_getPressedButton();
  switch(pressedButton) {
  case SIZE_ADD_BUTTON:
  case SIZE_SUB_BUTTON:
    if(!prevSizeButtonState) {
      prevSizeButtonState = true;
      commandArgs[0] = pressedButton;
      Command_send(COMMAND_SIZE_ADJUST_PRESS, 1);
    }
    break;
  case DELAY_X_ADD_BUTTON:
  case DELAY_X_SUB_BUTTON:
  case DELAY_Y_ADD_BUTTON:
  case DELAY_Y_SUB_BUTTON:
  case QUAN_LOW_BUTTON:
  case QUAN_MED_BUTTON:
  case QUAN_HIGH_BUTTON:
  case RUN_BUTTON:
  case EXIT_BUTTON:
    prevButton = pressedButton;
    break;
  default:
    if(prevSizeButtonState) {
      prevSizeButtonState = false;
      Command_send(COMMAND_SIZE_ADJUST_RELEASE);
    }
    else if(prevButton != -1) {
      if(prevButton == RUN_BUTTON) {
        Command_send(COMMAND_RUN);
        isRecover = false;
        switchScreen(RUN_SCREEN);
      }
      else if(prevButton == EXIT_BUTTON){
        Command_send(COMMAND_SETTING_EXIT);
      }
      else {
        commandArgs[0] = prevButton;
        Command_send(COMMAND_DELAY_QUAL_ADJUST, 1);
      }
      prevButton = -1;
    }
    break;
  }
  if(isCompleteCommand) {
    isCompleteCommand = false;
    switch(command[0]) {
    case COMMAND_NEW_SIZE:
      {
        int width = (command[1] - 20) << 7 | (command[2] - 20);
        int height = (command[3] - 20) << 7 | (command[4] - 20);
        TFT.setColor(RED);
        TFT.setBackColor(LIME);
        TFT.printNumI(width, 5+16*2, 200, 4);
        TFT.printNumI(height, 5+16*2, 200+16, 4);
        TFT.setBackColor(TRANSPARENT);
        break;
      }
    case COMMAND_NEW_DELAY_XY:
      {
        int delay_X = (command[1] - 20) << 7 | (command[2] - 20);
        int delay_Y = (command[3] - 20) << 7 | (command[4] - 20);
        TFT.setColor(RED);
        TFT.setBackColor(LIME);
        TFT.printNumI(delay_X, 112+16*2, 80, 4);
        TFT.printNumI(delay_Y, 112+16*2, 80+24+36, 4);
        TFT.setBackColor(TRANSPARENT);
        break;
      }
    case COMMAND_NEW_QUALITY:
      TFT.setColor(RED);
      TFT.setBackColor(YELLOW);
      switch(command[1]) {
      case QUAL_LOW:
        TFT.print(" Low ", 224, 16);
        break;
      case QUAL_MED:
        TFT.print(" Med.", 224, 16);
        break;
      case QUAL_HIGH:
        TFT.print(" High", 224, 16);
        break;
      }
      TFT.setBackColor(TRANSPARENT);
      break;
    }
  }
}




























