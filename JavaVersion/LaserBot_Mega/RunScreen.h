const byte RUNSCREEN_RESIZING = 0;
const byte RUNSCREEN_DITHERING = 1;
const byte RUNSCREEN_RUNING = 2;
byte RunScreen_state = RUNSCREEN_RESIZING;

boolean isRecover = false;

void RunScreen_init() {
  RunScreen_state = RUNSCREEN_RESIZING;
}

void RunScreen_draw() {
  TFT.fillScr(SILVER);
  TFT.setColor(WHITE);
  TFT.setBackColor(BLUE);
  TFT.setFont(BigFont);
  TFT.print("Resizing Image...", CENTER, 224);
  TFT.setBackColor(TRANSPARENT);
  Command_beginRecvImage(COMMAD_STATE_COLOR_IMAGE);
}

inline String timeVal(byte value) {
  if(value < 10) {
    return "0" + String(value);
  }
  else {
    return String(value);
  }
}

void RunScreen_proccess() {
  if(RunScreen_state == RUNSCREEN_RUNING) {
    if(isCompleteCommand) {
      isCompleteCommand = false;
      if(command[0] == COMMAND_RUN_STATE) {
        int oldX = (command[1] - 20) << 7 | (command[2] - 20);
        int newX = (command[3] - 20) << 7 | (command[4] - 20);
        TFT.setColor(RED);
        TFT.drawLine(oldX, 0, newX, 223);
      }
      else if(command[0] == COMMAND_RUN_TIME_INFO) {
        /*String establishTime = " Establish: " + timeVal(command[1]-20) + ":" + timeVal(command[2]-20) + ":" + timeVal(command[3]-20) + " ";
         String remainingTime = " Remaining: " + timeVal(command[4]-20) + ":" + timeVal(command[5]-20) + ":" + timeVal(command[6]-20) + " ";

         TFT.setColor(WHITE);
         TFT.setBackColor(BLUE);
         TFT.setFont(BigFont);
         TFT.print(establishTime, CENTER, 208);
         TFT.print(remainingTime, CENTER, 208+16);*/

        String time = timeVal(command[1]-20) + ":" + timeVal(command[2]-20) + ":" + timeVal(command[3]-20)
          + " / " + timeVal(command[4]-20) + ":" + timeVal(command[5]-20) + ":" + timeVal(command[6]-20) ;

        TFT.setColor(WHITE);
        TFT.setBackColor(BLUE);
        TFT.setFont(BigFont);
        TFT.print(time, CENTER, 224);
      }
      else if(command[0] == COMMAND_DRAWING_BREAK) {
        TFT.setFont(BiggerFont);
        TFT.setColor(RED);
        TFT.setBackColor(YELLOW);
        TFT.print("BREAK", CENTER, 104);
      }
    }
  }
  else if(RunScreen_state == RUNSCREEN_RESIZING) {
    if(commandState == COMMAD_STATE_NORMAL) {
      TFT.setFont(BigFont);
      TFT.setColor(WHITE);
      TFT.setBackColor(BLUE);
      TFT.print("Dithering Image...", CENTER, 224);
      TFT.setBackColor(TRANSPARENT);
      RunScreen_state = RUNSCREEN_DITHERING;
      Command_beginRecvImage(COMMAD_STATE_BW_IMAGE);
    }
  }
  else {
    if(commandState == COMMAD_STATE_NORMAL) {
      if(!isRecover) {
        TFT.setFont(BigFont);
        TFT.setColor(WHITE);
        TFT.setBackColor(BLUE);
        TFT.print("Saving Image...", CENTER, 224);
        TFT.setBackColor(TRANSPARENT);
      }
      RunScreen_state = RUNSCREEN_RUNING;
    }
  }
}





