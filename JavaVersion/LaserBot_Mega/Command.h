const byte COMMAND_BUFFER = 32;
const byte COMMAND_BAUD = 115200;

const byte COMMAND_START = 20;
const byte COMMAND_RECVFILE_FAIL = 21;
const byte COMMAND_RECVFILE_START = 22;
const byte COMMAND_RECVFILE_PROCCESS = 23;
const byte COMMAND_RECVFILE_END = 24;
const byte COMMAND_RECVFILE_LOAD_FINISH = 25;
const byte COMMAND_RECVFILE_GOTO_XY = 26;
const byte COMMAND_SWITCH_SETTING_SCREEN = 27;
const byte COMMAND_SIZE_ADJUST_PRESS = 28;
const byte COMMAND_SIZE_ADJUST_RELEASE = 29;
const byte COMMAND_NEW_SIZE = 30;
const byte COMMAND_DELAY_QUAL_ADJUST = 31;
const byte COMMAND_NEW_DELAY_XY = 32;
const byte COMMAND_NEW_QUALITY = 33;
const byte COMMAND_RUN = 34;
const byte COMMAND_RUN_DITHERING = 35;
const byte COMMAND_RUN_STATE = 36;
const byte COMMAND_RUN_TIME_INFO = 37;
const byte COMMAND_HOME = 38;
const byte COMMAND_ENABLE_RECOVER = 39;
const byte COMMAND_ENTER_RECOVER = 40;
const byte COMMAND_DRAWING_BREAK = 41;
const byte COMMAND_ENTER_RUN = 42;
const byte COMMAND_RESTART = 43;
const byte COMMAND_SETTING_EXIT = 44;
const byte COMMAND_SHUTDOWN = 45;

const byte QUAL_LOW = 0;
const byte QUAL_MED = 1;
const byte QUAL_HIGH = 2;

byte command[COMMAND_BUFFER];
byte commandArgs[COMMAND_BUFFER];
byte commandCount;
boolean isCompleteCommand = false;
boolean isStartedCommand = false;

const byte COMMAD_STATE_NORMAL = 0;
const byte COMMAD_STATE_COLOR_IMAGE = 1;
const byte COMMAD_STATE_BW_IMAGE = 2;
byte commandState = COMMAD_STATE_NORMAL;

void Command_process() {
  if(!isCompleteCommand) {
    while(Serial1.available()) {
      byte data = Serial1.read();
      if(isStartedCommand) {
        if (data == '\n') {
          isCompleteCommand = true;
          isStartedCommand = false;
          break;
        }
        else {
          command[commandCount++] = data;
        }
      }
      else if(data == '`') {
        isStartedCommand = true;
        isCompleteCommand = false;
        commandCount = 0;
      }
    }
  }
}


int ImageRecv_curX, ImageRecv_curY;
int ImageRecv_prevData;
boolean ImageRecv_isBeginSend;

void Command_beginRecvImage(byte type) {
  ImageRecv_curX = 0;
  ImageRecv_curY = 0;
  ImageRecv_prevData = -1;
  ImageRecv_isBeginSend = false;
  isCompleteCommand = false;
  isStartedCommand = false;
  commandState = type;
}

void Command_processColorImage() {
  while(Serial1.available()) {
    byte data = Serial1.read();
    if (ImageRecv_isBeginSend) {
      if(ImageRecv_prevData != -1){
        TFT.drawPixel(ImageRecv_curX, ImageRecv_curY, ImageRecv_prevData, data);
        ImageRecv_prevData = -1;
        ImageRecv_curX++;
        if(ImageRecv_curX == SCREEN_WIDTH) {
          ImageRecv_curX = 0;
          ImageRecv_curY++;
          if(ImageRecv_curY == SCREEN_HEIGHT) {
            commandState = COMMAD_STATE_NORMAL;
            break;
          }
        }
      }
      else {
        ImageRecv_prevData = data;
      }
    }
    else if (data == '~') {
      ImageRecv_isBeginSend = true;
    }
  }
}

void Command_processBWImage() {
  while(Serial1.available()) {
    byte data = Serial1.read();
    if (ImageRecv_isBeginSend) {
      TFT.drawPixel(ImageRecv_curX, ImageRecv_curY, data);
      ImageRecv_prevData = -1;
      ImageRecv_curX++;
      if(ImageRecv_curX == SCREEN_WIDTH) {
        ImageRecv_curX = 0;
        ImageRecv_curY++;
        if(ImageRecv_curY == SCREEN_HEIGHT) {
          commandState = COMMAD_STATE_NORMAL;
          break;
        }
      }
    }
    else if (data == '~') {
      ImageRecv_isBeginSend = true;
    }
  }
}

void Command_send(byte commandToSend, byte commandArgsCount) {
  Serial1.write('`');
  Serial1.write(commandToSend);
  for(byte i = 0; i < commandArgsCount; i++) {
    Serial1.write(commandArgs[i]);
  }
  Serial1.write('\n');
}

void Command_send(byte commandToSend) {
  Serial1.write('`');
  Serial1.write(commandToSend);
  Serial1.write('\n');
}













