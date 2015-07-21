//extern unsigned int logo[];

static boolean prevButtonState = false;

void StartScreen_process() {
  if(Touch_read() && MISC_isTouchBox_XY(70, 35, 245, 170)){
    Command_send(COMMAND_START);
    switchScreen(WAITFILE_SCREEN);
  }

  Button_proccess();
  boolean curButtonState = (Button_getPressedButton() != -1);
  if(curButtonState == false && prevButtonState == true) {
    isRecover = true;
    switchScreen(RUN_SCREEN);
    RunScreen_state = RUNSCREEN_DITHERING;
    Command_beginRecvImage(COMMAD_STATE_BW_IMAGE);
    Command_send(COMMAND_ENTER_RECOVER);
    TFT.setFont(BigFont);
    TFT.setColor(WHITE);
    TFT.setBackColor(BLUE);
    TFT.print(" Loading Image... ", CENTER, 224);
  }
  else {
    prevButtonState = curButtonState;
  }

  if(isCompleteCommand) {
    isCompleteCommand = false;
    if(command[0] == COMMAND_ENABLE_RECOVER) {
      Button_addNew("Recover", 190, 188, 120, 32, BLACK, RED, BLUE);
      Button_draw();
    }
  }
}

void StartScreen_init() {
  Button_clear();
  prevButtonState = false;
}

void StartScreen_draw() {
  //TFT.drawBitmap(33, 0, 127, 106, logo, 2);
  TFT.fillScr(YELLOW);

  TFT.setColor(RED);
  TFT.setFont(BiggerFont);
  TFT.print("WELCOME!", CENTER, 5);

  TFT.setColor(OLIVE);
  TFT.print("(START)", CENTER, 100);

  TFT.setColor(BLUE);
  TFT.print("LASER BOT", CENTER, 176);

  TFT.setColor(BLACK);
  TFT.setFont(BigFont);
  TFT.print("Copyright @ 2014", CENTER, 208);
  TFT.print("Nguyen Duong Kim Hao", CENTER, 224);
}



