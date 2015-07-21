const int WAITFILE_STARTX = 30;

void WaitFileScreen_draw() {
  TFT.fillScr(BLUE);

  TFT.setColor(YELLOW);
  TFT.setFont(BiggerFont);
  TFT.print("Wait for", CENTER, WAITFILE_STARTX);
  TFT.print("Image", CENTER, WAITFILE_STARTX+32);

  TFT.setColor(RED);
  TFT.setFont(BigFont);
  TFT.print("Please send image", CENTER, WAITFILE_STARTX+32+32 +10);
  TFT.print("to engrave", CENTER, WAITFILE_STARTX+32+32+10 +16);
}

void WaitFileScreen_proccess() {
  if(isCompleteCommand) {
    isCompleteCommand = false;
    switch(command[0]) {
    case COMMAND_RECVFILE_FAIL:
      TFT.setColor(RED);
      TFT.setBackColor(BLUE);
      TFT.setFont(BiggerFont);
      for(byte i = 0; i <= TIMEOUR_BLINK_TIMES; i++) {
        TFT.print("TIME OUT!", CENTER, WAITFILE_STARTX+32+32+10+16 +32);
        delay(TIMEOUR_BLINK_DELAY);
        if(i != TIMEOUR_BLINK_TIMES) {
          TFT.print("         ", CENTER, WAITFILE_STARTX+32+32+10+16 +32);
          delay(TIMEOUR_BLINK_DELAY);
        }
        else {
          switchScreen(START_SCREEN);
        }
      }
      break;
    case COMMAND_RECVFILE_START:
      ProccessBar_init(BLUE);
      break;
    case COMMAND_RECVFILE_PROCCESS:
      ProccessBar_draw(command[1]);
      break;
    case COMMAND_RECVFILE_END:
      WaitFileScreen_draw();
      TFT.setColor(RED);
      TFT.setFont(BiggerFont);
      TFT.print("FINISH!", CENTER, WAITFILE_STARTX+32+32+10+16 +32);
      TFT.setColor(WHITE);
      TFT.setFont(BigFont);
      TFT.print("Loading Image...", CENTER, WAITFILE_STARTX+32+32+10+16+32+32+16);
      break;
    case COMMAND_RECVFILE_LOAD_FINISH:
      TFT.setColor(WHITE);
      TFT.setBackColor(BLUE);
      TFT.print(" Finding Home... ", CENTER, WAITFILE_STARTX+32+32+10+16+32+32+16);
      break;
    case COMMAND_RECVFILE_GOTO_XY:
      TFT.setColor(WHITE);
      TFT.setBackColor(BLUE);
      TFT.print("Go to position...", CENTER, WAITFILE_STARTX+32+32+10+16+32+32+16);
      break;
    case COMMAND_SWITCH_SETTING_SCREEN:
      switchScreen(SETTING_SCREEN);
      break;
    case COMMAND_ENTER_RUN:
      isRecover = true;
      switchScreen(RUN_SCREEN);
      RunScreen_state = RUNSCREEN_DITHERING;
      Command_beginRecvImage(COMMAD_STATE_BW_IMAGE);
      TFT.setFont(BigFont);
      TFT.setColor(WHITE);
      TFT.setBackColor(BLUE);
      TFT.print(" Loading Image... ", CENTER, 224);
      break;
    }
  }
}

























