/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.gui;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import static laserbot_galileo.Common.*;
import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.Term;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import laserbot_galileo.laserDraw.Image;
import laserbot_galileo.svgPlotter.Path;
import laserbot_galileo.svgPlotter.SVGProcess;
import org.imgscalr.Scalr;

/**
 *
 * @author KimHao
 */
public class GUIMain extends javax.swing.JFrame {

    private final Validate loginValidate;
    private final Validate drawNowValidate;
    private final Validate svgValidate;

    private final JSch jsch = new JSch();
    private Session session;

    private ChannelShell shell;
    private OutputStream shellOut;
    private InputStream shellIn;
    private Connection connection;
    private Thread thread;

    private ChannelSftp sftp;
    private File tempFile;

    public GUIMain() {
        initComponents();

        setConnect(false);
        this.loginValidate = new Validate(
                new Object[]{txtIPAddress, "IP Address", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExIP, "###.###.###.###")},
                new Object[]{txtLoginUser, "Login User", Validate.getRequestValidate()},
                new Object[]{txtPassword, "Password", Validate.getRequestValidate()}
        );
        this.drawNowValidate = new Validate(
                new Object[]{txtDrawNowDelayX, "Delay X", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")},
                new Object[]{txtDrawNowDelayY, "Delay Y", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")},
                new Object[]{txtDrawNowWidth, "Width", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")},
                new Object[]{txtDrawNowHeight, "Height", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")}
        );
        this.svgValidate = new Validate(
                new Object[]{txtSVGDelayX, "Delay X", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")},
                new Object[]{txtSVGDelayY, "Delay Y", Validate.getRequestValidate(),
                    Validate.getRegExValidate(Validate.regExPositiveInteger, "positve number")},
                new Object[]{txtSVGDelayXY, "Delay XY", Validate.getRequestValidate()}
        );

        try {
            this.tempFile = File.createTempFile("LaserBot_GUI_" + System.currentTimeMillis(), "tempFile");
        } catch (IOException ex) {
            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneMain = new javax.swing.JTabbedPane();
        pConnect = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        txtLoginUser = new javax.swing.JTextField();
        txtIPAddress = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnTestDrawNow = new javax.swing.JButton();
        pTerminal = new javax.swing.JPanel();
        pAction = new javax.swing.JPanel();
        btnShutdown = new javax.swing.JButton();
        btnRestart = new javax.swing.JButton();
        btnCDLogs = new javax.swing.JButton();
        btnCDDir = new javax.swing.JButton();
        btnCDDrawNow = new javax.swing.JButton();
        btnRMDrawNow = new javax.swing.JButton();
        btnRMLog = new javax.swing.JButton();
        btnTermStart = new javax.swing.JButton();
        btnTermStop = new javax.swing.JButton();
        btnScreenRetach = new javax.swing.JButton();
        btnWatchRam = new javax.swing.JButton();
        btnLS = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        term = new com.jcraft.jcterm.JCTermSwing();
        pLogs = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listLogs = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        btnGetLogs = new javax.swing.JButton();
        lblFiles = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList();
        jTabbedPanePreview = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        taFileContent = new javax.swing.JTextArea();
        lblPictureLog = new javax.swing.JLabel();
        btnSaveFile = new javax.swing.JButton();
        btnSaveFolder = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnDrawNowOpen = new javax.swing.JButton();
        pDrawNowPreview = new javax.swing.JLabel();
        pDrawNowAction = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbDrawNowQuality = new javax.swing.JComboBox();
        lblWidth = new javax.swing.JLabel();
        txtDrawNowWidth = new javax.swing.JTextField();
        lblHeight = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDrawNowHeight = new javax.swing.JTextField();
        txtDrawNowDelayX = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtDrawNowDelayY = new javax.swing.JTextField();
        btnProcess = new javax.swing.JButton();
        btnDrawNowClear = new javax.swing.JButton();
        btnDrawNowSend = new javax.swing.JButton();
        sBrightness = new javax.swing.JSpinner();
        sContrast = new javax.swing.JSpinner();
        lblWidth1 = new javax.swing.JLabel();
        lblWidth2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSVGOpen = new javax.swing.JButton();
        pSVGAction = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbSVGScanline = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtSVGDelayX = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSVGDelayY = new javax.swing.JTextField();
        btnSVGProcess = new javax.swing.JButton();
        btnSVGClear = new javax.swing.JButton();
        btnSVGSend = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtSVGDelayXY = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cbSVGMaxScanline = new javax.swing.JComboBox();
        sPrecision = new javax.swing.JSpinner();
        sSegments = new javax.swing.JSpinner();
        lblWidth3 = new javax.swing.JLabel();
        lblWidth4 = new javax.swing.JLabel();
        pSVGPreview = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Laser Bot SSH Control");
        setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        setResizable(false);

        jTabbedPaneMain.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        pConnect.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnConnect.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnConnect.setText("Connect");
        btnConnect.setFocusable(false);
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnDisconnect.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDisconnect.setText("Disconnect");
        btnDisconnect.setFocusable(false);
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        txtPassword.setText("root");

        txtLoginUser.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtLoginUser.setText("root");

        txtIPAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtIPAddress.setText("192.168.1.105");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Host ip: ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Login user:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Password:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLoginUser, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(btnConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDisconnect)))
                .addGap(51, 51, 51))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtLoginUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConnect)
                    .addComponent(btnDisconnect))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        btnTestDrawNow.setText("Test Draw Now");
        btnTestDrawNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestDrawNowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pConnectLayout = new javax.swing.GroupLayout(pConnect);
        pConnect.setLayout(pConnectLayout);
        pConnectLayout.setHorizontalGroup(
            pConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConnectLayout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(301, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConnectLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnTestDrawNow))
        );
        pConnectLayout.setVerticalGroup(
            pConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConnectLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                .addComponent(btnTestDrawNow))
        );

        jTabbedPaneMain.addTab("Connect", pConnect);

        btnShutdown.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnShutdown.setText("shutdown");
        btnShutdown.setFocusable(false);
        btnShutdown.setPreferredSize(new java.awt.Dimension(120, 25));
        btnShutdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShutdownActionPerformed(evt);
            }
        });

        btnRestart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRestart.setText("restart");
        btnRestart.setFocusable(false);
        btnRestart.setPreferredSize(new java.awt.Dimension(120, 25));
        btnRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestartActionPerformed(evt);
            }
        });

        btnCDLogs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCDLogs.setText("cd logs");
        btnCDLogs.setFocusable(false);
        btnCDLogs.setPreferredSize(new java.awt.Dimension(120, 25));
        btnCDLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCDLogsActionPerformed(evt);
            }
        });

        btnCDDir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCDDir.setText("cd rundir");
        btnCDDir.setFocusable(false);
        btnCDDir.setPreferredSize(new java.awt.Dimension(120, 25));
        btnCDDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCDDirActionPerformed(evt);
            }
        });

        btnCDDrawNow.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCDDrawNow.setText("cd drawNow");
        btnCDDrawNow.setFocusable(false);
        btnCDDrawNow.setPreferredSize(new java.awt.Dimension(120, 25));
        btnCDDrawNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCDDrawNowActionPerformed(evt);
            }
        });

        btnRMDrawNow.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRMDrawNow.setText("rm drawNow");
        btnRMDrawNow.setFocusable(false);
        btnRMDrawNow.setPreferredSize(new java.awt.Dimension(120, 25));
        btnRMDrawNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRMDrawNowActionPerformed(evt);
            }
        });

        btnRMLog.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRMLog.setText("rm log");
        btnRMLog.setFocusable(false);
        btnRMLog.setPreferredSize(new java.awt.Dimension(120, 25));
        btnRMLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRMLogActionPerformed(evt);
            }
        });

        btnTermStart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTermStart.setText("start");
        btnTermStart.setFocusable(false);
        btnTermStart.setPreferredSize(new java.awt.Dimension(120, 25));
        btnTermStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTermStartActionPerformed(evt);
            }
        });

        btnTermStop.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTermStop.setText("stop");
        btnTermStop.setFocusable(false);
        btnTermStop.setPreferredSize(new java.awt.Dimension(120, 25));
        btnTermStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTermStopActionPerformed(evt);
            }
        });

        btnScreenRetach.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnScreenRetach.setText("screen retach");
        btnScreenRetach.setFocusable(false);
        btnScreenRetach.setPreferredSize(new java.awt.Dimension(120, 25));
        btnScreenRetach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScreenRetachActionPerformed(evt);
            }
        });

        btnWatchRam.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnWatchRam.setText("watch ram");
        btnWatchRam.setFocusable(false);
        btnWatchRam.setPreferredSize(new java.awt.Dimension(120, 25));
        btnWatchRam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWatchRamActionPerformed(evt);
            }
        });

        btnLS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLS.setText("ls");
        btnLS.setFocusable(false);
        btnLS.setPreferredSize(new java.awt.Dimension(120, 25));
        btnLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pActionLayout = new javax.swing.GroupLayout(pAction);
        pAction.setLayout(pActionLayout);
        pActionLayout.setHorizontalGroup(
            pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pActionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnShutdown, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRestart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pActionLayout.createSequentialGroup()
                        .addComponent(btnCDLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCDDrawNow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pActionLayout.createSequentialGroup()
                        .addComponent(btnCDDir, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRMDrawNow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRMLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnScreenRetach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnWatchRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTermStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTermStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pActionLayout.setVerticalGroup(
            pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pActionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pActionLayout.createSequentialGroup()
                            .addComponent(btnLS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31))
                        .addComponent(btnWatchRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pActionLayout.createSequentialGroup()
                            .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnTermStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnScreenRetach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnTermStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pActionLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(btnRMLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pActionLayout.createSequentialGroup()
                                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnShutdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCDLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCDDrawNow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRestart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCDDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRMDrawNow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("jcterm-0.0.11: jcraft.com");
        jLabel5.setToolTipText("");

        term.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                termMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout termLayout = new javax.swing.GroupLayout(term);
        term.setLayout(termLayout);
        termLayout.setHorizontalGroup(
            termLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
        );
        termLayout.setVerticalGroup(
            termLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pTerminalLayout = new javax.swing.GroupLayout(pTerminal);
        pTerminal.setLayout(pTerminalLayout);
        pTerminalLayout.setHorizontalGroup(
            pTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTerminalLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(pAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(0, 19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTerminalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(term, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
        pTerminalLayout.setVerticalGroup(
            pTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTerminalLayout.createSequentialGroup()
                .addGroup(pTerminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pTerminalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pTerminalLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(term, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneMain.addTab("Terminal", pTerminal);

        listLogs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        listLogs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listLogs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listLogsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listLogs);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Current logs:");

        btnGetLogs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGetLogs.setText("Refresh Logs");
        btnGetLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshLogsActionPerformed(evt);
            }
        });

        lblFiles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFiles.setText("Files:");

        listFiles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        listFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listFilesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listFiles);

        taFileContent.setEditable(false);
        taFileContent.setColumns(20);
        taFileContent.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        taFileContent.setRows(5);
        jScrollPane3.setViewportView(taFileContent);

        jTabbedPanePreview.addTab("Text", jScrollPane3);
        jTabbedPanePreview.addTab("Image", lblPictureLog);

        btnSaveFile.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveFile.setText("Save File");
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFileActionPerformed(evt);
            }
        });

        btnSaveFolder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveFolder.setText("Save Folder");
        btnSaveFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFolderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pLogsLayout = new javax.swing.GroupLayout(pLogs);
        pLogs.setLayout(pLogsLayout);
        pLogsLayout.setHorizontalGroup(
            pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLogsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGetLogs, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(btnSaveFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnSaveFile, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                    .addComponent(lblFiles))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPanePreview, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addContainerGap())
        );
        pLogsLayout.setVerticalGroup(
            pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pLogsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGetLogs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLogsLayout.createSequentialGroup()
                        .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lblFiles))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pLogsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSaveFile)
                            .addComponent(btnSaveFolder)))
                    .addComponent(jTabbedPanePreview))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Logs", pLogs);

        btnDrawNowOpen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDrawNowOpen.setText("Open Image");
        btnDrawNowOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawNowOpenActionPerformed(evt);
            }
        });

        pDrawNowPreview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pDrawNowPreviewMouseClicked(evt);
            }
        });

        pDrawNowAction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pDrawNowAction.setVisible(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Quality:");

        cbDrawNowQuality.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbDrawNowQuality.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Low", "Medium", "High" }));

        lblWidth.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblWidth.setText("Width (1/10mm):");
        lblWidth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblWidthMouseClicked(evt);
            }
        });

        txtDrawNowWidth.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblHeight.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblHeight.setText("Height (1/10mm):");
        lblHeight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHeightMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Delay X:");

        txtDrawNowHeight.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        txtDrawNowDelayX.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Delay Y:");

        txtDrawNowDelayY.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnProcess.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        btnDrawNowClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDrawNowClear.setText("Clear");
        btnDrawNowClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawNowClearActionPerformed(evt);
            }
        });

        btnDrawNowSend.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDrawNowSend.setText("Send");
        btnDrawNowSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawNowSendActionPerformed(evt);
            }
        });

        sBrightness.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sBrightness.setModel(new javax.swing.SpinnerNumberModel(0, -100, 100, 1));

        sContrast.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sContrast.setModel(new javax.swing.SpinnerNumberModel(0, -100, 500, 1));

        lblWidth1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblWidth1.setText("Brightness:");
        lblWidth1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblWidth1MouseClicked(evt);
            }
        });

        lblWidth2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblWidth2.setText("Contrast:");
        lblWidth2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblWidth2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pDrawNowActionLayout = new javax.swing.GroupLayout(pDrawNowAction);
        pDrawNowAction.setLayout(pDrawNowActionLayout);
        pDrawNowActionLayout.setHorizontalGroup(
            pDrawNowActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDrawNowActionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pDrawNowActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDrawNowClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProcess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDrawNowDelayX)
                    .addComponent(txtDrawNowDelayY)
                    .addComponent(cbDrawNowQuality, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDrawNowWidth)
                    .addComponent(txtDrawNowHeight)
                    .addComponent(btnDrawNowSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sBrightness)
                    .addComponent(sContrast)
                    .addGroup(pDrawNowActionLayout.createSequentialGroup()
                        .addGroup(pDrawNowActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(lblWidth)
                            .addComponent(lblHeight)
                            .addComponent(lblWidth1)
                            .addComponent(lblWidth2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pDrawNowActionLayout.setVerticalGroup(
            pDrawNowActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDrawNowActionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawNowDelayX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawNowDelayY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDrawNowQuality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(lblWidth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawNowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHeight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawNowHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblWidth1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sBrightness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblWidth2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sContrast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btnProcess)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDrawNowSend)
                .addGap(18, 18, 18)
                .addComponent(btnDrawNowClear)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pDrawNowPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pDrawNowAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDrawNowOpen)
                        .addGap(15, 15, 15)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pDrawNowPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDrawNowOpen)
                        .addGap(18, 18, 18)
                        .addComponent(pDrawNowAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Draw Now", jPanel1);

        btnSVGOpen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSVGOpen.setText("Open SVG File");
        btnSVGOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVGOpenActionPerformed(evt);
            }
        });

        pSVGAction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pSVGAction.setVisible(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Scanline:");

        cbSVGScanline.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbSVGScanline.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cbSVGScanline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSVGScanlineActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Delay X:");

        txtSVGDelayX.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Delay Y:");

        txtSVGDelayY.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnSVGProcess.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSVGProcess.setText("Process");
        btnSVGProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVGProcessActionPerformed(evt);
            }
        });

        btnSVGClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSVGClear.setText("Clear");
        btnSVGClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVGClearActionPerformed(evt);
            }
        });

        btnSVGSend.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSVGSend.setText("Send");
        btnSVGSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVGSendActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Delay XY:");

        txtSVGDelayXY.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Max Scanline:");

        cbSVGMaxScanline.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbSVGMaxScanline.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));

        sPrecision.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sPrecision.setModel(new javax.swing.SpinnerNumberModel(1, 1, 2000, 1));

        sSegments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sSegments.setModel(new javax.swing.SpinnerNumberModel(1, 1, 2000, 1));

        lblWidth3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblWidth3.setText("Precision:");
        lblWidth3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblWidth3MouseClicked(evt);
            }
        });

        lblWidth4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblWidth4.setText("Segments:");
        lblWidth4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblWidth4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pSVGActionLayout = new javax.swing.GroupLayout(pSVGAction);
        pSVGAction.setLayout(pSVGActionLayout);
        pSVGActionLayout.setHorizontalGroup(
            pSVGActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSVGActionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pSVGActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSVGClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSVGProcess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSVGDelayX)
                    .addComponent(txtSVGDelayY)
                    .addComponent(btnSVGSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSVGDelayXY)
                    .addComponent(cbSVGScanline, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSVGMaxScanline, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sPrecision)
                    .addComponent(sSegments)
                    .addGroup(pSVGActionLayout.createSequentialGroup()
                        .addGroup(pSVGActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel9)
                            .addComponent(jLabel13)
                            .addComponent(lblWidth3)
                            .addComponent(lblWidth4))
                        .addGap(0, 29, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pSVGActionLayout.setVerticalGroup(
            pSVGActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSVGActionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSVGDelayX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSVGDelayY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSVGDelayXY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSVGScanline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSVGMaxScanline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblWidth3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sPrecision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblWidth4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sSegments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btnSVGProcess)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSVGSend)
                .addGap(18, 18, 18)
                .addComponent(btnSVGClear)
                .addContainerGap())
        );

        pSVGPreview.setAutoscrolls(true);
        pSVGPreview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pSVGPreviewMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pSVGPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btnSVGOpen))
                    .addComponent(pSVGAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pSVGPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSVGOpen)
                        .addGap(18, 18, 18)
                        .addComponent(pSVGAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Draw Now SVG", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        if (loginValidate.validate(this)) {
            String user = txtLoginUser.getText();
            String host = txtIPAddress.getText();

            try {

                session = jsch.getSession(user, host, 22);
                session.setPassword(new String(txtPassword.getPassword()));
                session.setUserInfo(ui);
                session.setConfig("cipher.s2c", "none,aes128-cbc,3des-cbc,blowfish-cbc");
                session.setConfig("cipher.c2s", "none,aes128-cbc,3des-cbc,blowfish-cbc");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(30000);

                shell = (ChannelShell) session.openChannel("shell");
                shell.connect();
                shellOut = shell.getOutputStream();
                shellIn = shell.getInputStream();

                sftp = (ChannelSftp) session.openChannel("sftp");
                sftp.connect();

                setConnect(true);
                btnRefreshLogsActionPerformed(null);
                //ui.showMessage("Connected !");
            } catch (JSchException | IOException ex) {
                ui.showError(ex);
            }

            connection = new Connection() {
                @Override
                public InputStream getInputStream() {
                    return shellIn;
                }

                @Override
                public OutputStream getOutputStream() {
                    return shellOut;
                }

                @Override
                public void requestResize(Term term) {
                    if (shell instanceof ChannelShell) {
                        int c = term.getColumnCount();
                        int r = term.getRowCount();
                        shell.setPtySize(c, r, c * term.getCharWidth(),
                                r * term.getCharHeight());
                    }
                }

                @Override
                public void close() {
                    shell.disconnect();
                }
            };
            thread = new Thread(() -> {
                term.requestFocus();
                term.resetAllAttributes();
                term.start(connection);
                btnDisconnectActionPerformed(null);
            });
            thread.start();
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void setConnect(boolean state) {
        btnConnect.setEnabled(!state);
        txtIPAddress.setEnabled(!state);
        txtLoginUser.setEnabled(!state);
        txtPassword.setEnabled(!state);
        if (!state) {
            btnDrawNowClearActionPerformed(null);
            btnSVGClearActionPerformed(null);
        }

        btnDisconnect.setEnabled(state);
        jTabbedPaneMain.setEnabledAt(1, state);
        jTabbedPaneMain.setEnabledAt(2, state);
        jTabbedPaneMain.setEnabledAt(3, state);
        jTabbedPaneMain.setEnabledAt(4, state);

        jTabbedPaneMain.setSelectedIndex(0);
    }

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        setConnect(false);
        if (shell != null) {
            shell.disconnect();
            shell = null;
            ui.showMessage("Disconnected !");
        }
        if (sftp != null) {
            sftp.disconnect();
            sftp = null;
        }
        if (session != null) {
            session.disconnect();
            session = null;
        }
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private void btnShutdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShutdownActionPerformed
        if (ui.promptYesNo("Sure shutdown ?")) {
            term.sendCommand("shutdown -h now");
        }
    }//GEN-LAST:event_btnShutdownActionPerformed

    private void btnRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestartActionPerformed
        if (ui.promptYesNo("Sure restart ?")) {
            term.sendCommand("shutdown -r now");
        }
    }//GEN-LAST:event_btnRestartActionPerformed

    private static final String LOG_PATH = GALILEO_PATH + "logs/";
    private void btnCDLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCDLogsActionPerformed
        term.sendCommand("cd " + LOG_PATH);
    }//GEN-LAST:event_btnCDLogsActionPerformed

    private void btnCDDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCDDirActionPerformed
        term.sendCommand("cd " + GALILEO_PATH);
    }//GEN-LAST:event_btnCDDirActionPerformed

    private String[] arrLogFile;
    private void btnRefreshLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshLogsActionPerformed
        jTabbedPanePreview.setEnabledAt(0, false);
        jTabbedPanePreview.setEnabledAt(1, false);
        jTabbedPanePreview.setSelectedIndex(0);
        try {
            DefaultListModel model = new DefaultListModel();
            List<LsEntry> logsEntry = sftp.ls(LOG_PATH);
            ArrayList<String> tmpArrLog = new ArrayList<>();
            logsEntry.stream().sorted((e1, e2) -> {
                return e1.getFilename().compareTo(e2.getFilename());
            })
                    .filter((e) -> (e.getFilename().startsWith("log-")))
                    .forEach((e) -> {
                        try {
                            String elem = e.getFilename();
                            tmpArrLog.add(elem);
                            List<LsEntry> logContent = sftp.ls(LOG_PATH + elem);
                            for (LsEntry data : logContent) {
                                if (data.getFilename().equals("laserControl.array")) {
                                    elem += " (laser draw)";
                                    break;
                                } else if (data.getFilename().equals("xyData.array")) {
                                    elem += " (svg plotter)";
                                    break;
                                } else if (data.getFilename().startsWith("recovered-")) {
                                    elem += " (recovered)";
                                    break;
                                }
                            }
                            model.addElement(elem);
                        } catch (SftpException ex) {
                            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            listLogs.setModel(model);
            arrLogFile = tmpArrLog.toArray(new String[tmpArrLog.size()]);
        } catch (SftpException ex) {
            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRefreshLogsActionPerformed

    private int selectedLogIndex;
    private String curLogPath;
    private String[] arrFile;
    private void listLogsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listLogsValueChanged
        DefaultListModel model = new DefaultListModel();
        selectedLogIndex = listLogs.getSelectedIndex();
        if (selectedLogIndex == -1) {
            lblFiles.setText("Files:");
        } else {
            String name = arrLogFile[selectedLogIndex];
            try {
                ArrayList<String> tmpArr = new ArrayList<>();
                List<LsEntry> logsEntry = sftp.ls(curLogPath = (LOG_PATH + name + "/"));
                logsEntry.stream().sorted((e1, e2) -> {
                    return e1.getFilename().compareTo(e2.getFilename());
                })
                        .filter((e) -> (!e.getFilename().equals(".") && !e.getFilename().equals("..")))
                        .forEach((e) -> {
                            String filename = e.getFilename();
                            tmpArr.add(filename);
                            if (filename.equals("log.txt") || filename.equals("lcdImage.gif")) {
                                filename = "* " + filename;
                            }
                            model.addElement(filename + " (" + e.getAttrs().getSize() / 1024 + "KB)");
                        });
                lblFiles.setText("Files (" + name + "):");
                arrFile = tmpArr.toArray(new String[tmpArr.size()]);
            } catch (SftpException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        listFiles.setModel(model);
    }//GEN-LAST:event_listLogsValueChanged

    private boolean isLoadedFile;
    private void listFilesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listFilesValueChanged
        int selectedIndex = listFiles.getSelectedIndex();
        taFileContent.setText("");
        jTabbedPanePreview.setEnabledAt(0, false);
        jTabbedPanePreview.setEnabledAt(1, false);
        jTabbedPanePreview.setSelectedIndex(0);
        isLoadedFile = false;
        if (selectedIndex != -1) {
            try {
                String fileName = arrFile[selectedIndex];
                if (fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".png")
                        || fileName.endsWith(".txt") || fileName.endsWith(".svg") || fileName.endsWith(".properties")) {
                    sftp.get(curLogPath + fileName, tempFile.getPath(),
                            new MyProgressMonitor(), ChannelSftp.OVERWRITE);

                    if (fileName.endsWith(".txt") || fileName.endsWith(".svg") || fileName.endsWith(".properties")) {
                        BufferedReader buff = new BufferedReader(new FileReader(tempFile));
                        String str;
                        while ((str = buff.readLine()) != null) {
                            taFileContent.append(str + "\n");
                        }
                        jTabbedPanePreview.setEnabledAt(0, true);
                        jTabbedPanePreview.setSelectedIndex(0);
                    } else {
                        Image img = new Image(tempFile.getPath());
                        img.newWidth = lblPictureLog.getWidth();
                        img.newHeight = lblPictureLog.getHeight();
                        img.resize(Scalr.Method.BALANCED);
                        lblPictureLog.setIcon(new ImageIcon(img.image));
                        jTabbedPanePreview.setEnabledAt(1, true);
                        jTabbedPanePreview.setSelectedIndex(1);
                    }
                } else {
                    taFileContent.append("File not support preview !");
                }
            } catch (IOException | SftpException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_listFilesValueChanged

    private void btnSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFileActionPerformed
        int selectedIndex = listFiles.getSelectedIndex();
        if (selectedIndex != -1) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Specify a file to save");
            String filename = arrFile[selectedIndex];
            fc.setSelectedFile(new File(COMPUTER_PATH + filename));
            int retVal = fc.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                try {
                    if (isLoadedFile) {
                        Files.copy(tempFile.toPath(), fileToSave.toPath());
                    } else {
                        sftp.get(curLogPath + filename, fileToSave.getPath(),
                                new MyProgressMonitor(), ChannelSftp.OVERWRITE);
                    }
                } catch (IOException | SftpException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSaveFileActionPerformed

    private void btnSaveFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFolderActionPerformed
        int selectedIndex = listLogs.getSelectedIndex();
        if (selectedIndex != -1) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Specify a folder to save");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String logname = arrLogFile[selectedIndex];
            fc.setSelectedFile(new File(COMPUTER_PATH));
            int retVal = fc.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File folderToSave = new File(fc.getSelectedFile().getPath() + "\\" + logname);
                    folderToSave.mkdir();

                    List<LsEntry> logsEntry = sftp.ls(curLogPath);
                    logsEntry.stream()
                            .filter((e) -> (!e.getFilename().equals(".") && !e.getFilename().equals("..")))
                            .forEach((e) -> {
                                try {
                                    String filename = e.getFilename();
                                    sftp.get(curLogPath + filename, folderToSave.getPath() + "\\" + filename,
                                            new MyProgressMonitor(), ChannelSftp.OVERWRITE);
                                } catch (SftpException ex) {
                                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                    ui.showMessage("Save folder done !");
                } catch (SftpException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSaveFolderActionPerformed

    private final File drawNowFile = new File(DRAWNOW_PATH + "image.jpg");
    private double drawNowRatio;
    private void btnDrawNowOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawNowOpenActionPerformed
        pDrawNowAction.setVisible(false);
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Specify a file to open");
        fc.setSelectedFile(new File(COMPUTER_PATH));
        fc.setFileFilter(new FileNameExtensionFilter("JPG, JPEG, GIF, PNG, BMP Images",
                "jpg", "jpeg", "gif", "png", "bmp"));
        fc.setAcceptAllFileFilterUsed(false);
        int retVal = fc.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File fileToCopy = fc.getSelectedFile();
            try {
                if (!fileToCopy.equals(drawNowFile)) {
                    if (drawNowFile.exists()) {
                        drawNowFile.delete();
                    }
                    Files.copy(fileToCopy.toPath(), drawNowFile.toPath());
                }

                drawNowPreview = null;
                BufferedImage bImage = ImageIO.read(drawNowFile);

                drawNowRatio = (double) bImage.getWidth() / (double) bImage.getHeight();
                txtDrawNowWidth.setText(String.valueOf(bImage.getWidth()));
                txtDrawNowHeight.setText(String.valueOf(bImage.getHeight()));
                pDrawNowAction.setVisible(true);

                Image img = new Image(bImage);
                img.newWidth = pDrawNowPreview.getWidth();
                img.newHeight = pDrawNowPreview.getHeight();
                img.resize(Scalr.Method.BALANCED);
                pDrawNowPreview.setIcon(new ImageIcon(img.image));

                txtDrawNowDelayX.setText(String.valueOf(DEFAULT_X_DELAY));
                txtDrawNowDelayY.setText(String.valueOf(DEFAULT_Y_DELAY));
                cbDrawNowQuality.setSelectedIndex(DEFAULT_QUALITY);
                sBrightness.setValue(0);
                sContrast.setValue(0);
            } catch (IOException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnDrawNowOpenActionPerformed

    private BufferedImage drawNowPreview;
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        if (!drawNowValidate.validate(this)) {
            return;
        }
        int quality = cbDrawNowQuality.getSelectedIndex();
        int step_X = 1, step_Y = 1;
        switch (quality) {
            case QUAL_HIGH:
                step_X = 2;
                step_Y = 1;
                break;
            case QUAL_MED:
                step_X = 4;
                step_Y = 2;
                break;
            case QUAL_LOW:
                step_X = 6;
                step_Y = 3;
                break;
        }
        try {
            BufferedImage bImage = ImageIO.read(drawNowFile);
            double brightness = (double) (Integer) sBrightness.getValue();
            double contrast = (double) (Integer) sContrast.getValue();
            if (brightness != 0 || contrast != 0) {
                brightness /= 100;
                contrast = (100 + contrast) / 100;
                contrast *= contrast;

                byte[] buf = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < buf.length; i++) {
                    double c = (buf[i] & 0xFF) / 255f;
                    c = (c - 0.5) * contrast + 0.5;
                    c += brightness;
                    buf[i] = (byte) Math.min(Math.max(c * 255, 0), 255);
                }
            }
            Image image = new Image(bImage);

            image.newWidth = Integer.parseInt(txtDrawNowWidth.getText()) * 4 / step_X;
            image.newHeight = Integer.parseInt(txtDrawNowHeight.getText()) * 2 / step_Y;

            Image thumb = image.createThumbnail(Scalr.Method.ULTRA_QUALITY);
            thumb.grayscale();
            thumb.save(DRAWNOW_PATH + "lcdImage.gif");

            image.resize(Scalr.Method.QUALITY);
            image.dither();

            drawNowPreview = getPreview(image.newWidth, image.newHeight, image.laserControl);
            Image img = new Image(drawNowPreview);
            img.newWidth = pDrawNowPreview.getWidth();
            img.newHeight = pDrawNowPreview.getHeight();
            img.resize(Scalr.Method.QUALITY, false);
            pDrawNowPreview.setIcon(new ImageIcon(img.image));

            int delay_X = Integer.parseInt(txtDrawNowDelayX.getText());
            int delay_Y = Integer.parseInt(txtDrawNowDelayY.getText());
            image.saveLaserControl(DRAWNOW_PATH + "laserControl.array", thumb.oldWidth, delay_X, delay_Y, quality);
            ui.showMessage("Actual width = " + image.newWidth + "\n"
                    + "Actual height = " + image.newHeight + "\n"
                    + "Array size = " + (image.laserControl.length * image.laserControl[0].length)
            );
        } catch (IOException ex) {
            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private BufferedImage getPreview(int width, int height, short[][] laserControl) {
        BufferedImage preview = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = preview.createGraphics();
        File backFile = new File(DRAWNOW_PATH + "background.jpg");
        if (backFile.exists()) {
            try {
                BufferedImage backImage = ImageIO.read(backFile);
                for (int i = 0; i < preview.getWidth(); i += backImage.getWidth()) {
                    for (int j = 0; j < preview.getHeight(); j += backImage.getHeight()) {
                        g2d.drawImage(backImage, i, j, null);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        g2d.dispose();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (laserControl[i][j] == 0) {
                    preview.setRGB(i, j, 0xFF000000);
                }
            }
        }

        return preview;
    }

    private void lblWidthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblWidthMouseClicked
        if (drawNowValidate.validateControl(txtDrawNowHeight, this)) {
            int height = Integer.parseInt(txtDrawNowHeight.getText());
            txtDrawNowWidth.setText(Integer.toString((int) (height * drawNowRatio)));
        }
    }//GEN-LAST:event_lblWidthMouseClicked

    private void lblHeightMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHeightMouseClicked
        if (drawNowValidate.validateControl(txtDrawNowWidth, this)) {
            int width = Integer.parseInt(txtDrawNowWidth.getText());
            txtDrawNowHeight.setText(Integer.toString((int) (width / drawNowRatio)));
        }
    }//GEN-LAST:event_lblHeightMouseClicked

    private void pDrawNowPreviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pDrawNowPreviewMouseClicked
        if (drawNowPreview != null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Specify a file to save");
            fc.setFileFilter(new FileNameExtensionFilter("JPG, JPEG, GIF, PNG, BMP Images",
                    "jpg", "jpeg", "gif", "png", "bmp"));
            fc.setSelectedFile(new File(DRAWNOW_PATH + "preview.jpg"));
            int retVal = fc.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                try {
                    ImageIO.write(drawNowPreview, getFileExtension(fileToSave), fileToSave);
                } catch (IOException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_pDrawNowPreviewMouseClicked

    private void btnDrawNowClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawNowClearActionPerformed
        if (drawNowPreview != null) {
            drawNowPreview.flush();
            drawNowPreview = null;
        }
        pDrawNowAction.setVisible(false);
        pDrawNowPreview.setIcon(null);
    }//GEN-LAST:event_btnDrawNowClearActionPerformed

    private void btnDrawNowSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawNowSendActionPerformed
        if (session == null || !session.isConnected()) {
            ui.showMessage("You must connect first !");
        } else if (drawNowPreview == null) {
            ui.showMessage("You must process picture first !");
        } else {
            try {
                sftp.put(drawNowFile.getPath(), GALILEO_PATH + "drawNow/image.jpg", ChannelSftp.OVERWRITE);
                sftp.put(DRAWNOW_PATH + "laserControl.array",
                        GALILEO_PATH + "drawNow/laserControl.array", ChannelSftp.OVERWRITE);
                sftp.put(DRAWNOW_PATH + "lcdImage.gif",
                        GALILEO_PATH + "drawNow/lcdImage.gif", ChannelSftp.OVERWRITE);
            } catch (SftpException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnDrawNowSendActionPerformed

    private void btnCDDrawNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCDDrawNowActionPerformed
        term.sendCommand("cd " + GALILEO_PATH + "/drawNow");
    }//GEN-LAST:event_btnCDDrawNowActionPerformed

    private void btnScreenRetachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScreenRetachActionPerformed
        term.sendCommand("screen -r");
    }//GEN-LAST:event_btnScreenRetachActionPerformed

    private void btnRMDrawNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRMDrawNowActionPerformed
        term.sendCommand("rm " + GALILEO_PATH + "/drawNow/*");
    }//GEN-LAST:event_btnRMDrawNowActionPerformed

    private void btnRMLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRMLogActionPerformed
        term.sendCommand("rm -r " + GALILEO_PATH + "/logs/*");
        term.sendCommand("echo > " + GALILEO_PATH + "/logs/log.properties");
    }//GEN-LAST:event_btnRMLogActionPerformed

    private void btnTermStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTermStartActionPerformed
        term.sendCommand("/root/startup.sh");
    }//GEN-LAST:event_btnTermStartActionPerformed

    private void btnTermStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTermStopActionPerformed
        term.sendCommand("screen -X quit");
    }//GEN-LAST:event_btnTermStopActionPerformed

    private final File svgFile = new File(DRAWNOW_SVG_PATH + "drawing.svg");
    private void btnSVGOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVGOpenActionPerformed
        pSVGAction.setVisible(false);
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Specify a file to open");
        fc.setSelectedFile(new File(COMPUTER_PATH));
        fc.setFileFilter(new FileNameExtensionFilter("SVG Vector", "svg"));
        fc.setAcceptAllFileFilterUsed(false);
        int retVal = fc.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File fileToCopy = fc.getSelectedFile();
            try {
                if (!fileToCopy.equals(svgFile)) {
                    if (svgFile.exists()) {
                        svgFile.delete();
                    }
                    Files.copy(fileToCopy.toPath(), svgFile.toPath());
                }
                pSVGAction.setVisible(true);
                txtSVGDelayX.setText(String.valueOf(DEFAULT_STEP_DRAWLINE_X_DELAY));
                txtSVGDelayY.setText(String.valueOf(DEFAULT_STEP_DRAWLINE_Y_DELAY));
                txtSVGDelayXY.setText(String.valueOf(DEFAULT_STEP_DRAWLINE_XY_DELAY));
                cbSVGScanline.setSelectedIndex(0);
                cbSVGMaxScanline.setSelectedIndex(2);
                sPrecision.setValue(DEFAULT_PRECISION);
                sSegments.setValue(DEFAULT_SEGMENTS);
            } catch (IOException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSVGOpenActionPerformed

    private void btnSVGSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVGSendActionPerformed
        if (session == null || !session.isConnected()) {
            ui.showMessage("You must connect first !");
        } else if (SVGProcess.preview == null) {
            ui.showMessage("You must process svg file first !");
        } else {
            try {
                sftp.put(svgFile.getPath(), GALILEO_PATH + "drawNow/drawing.svg", ChannelSftp.OVERWRITE);
                sftp.put(DRAWNOW_SVG_PATH + "xyData.array",
                        GALILEO_PATH + "drawNow/xyData.array", ChannelSftp.OVERWRITE);
                sftp.put(DRAWNOW_SVG_PATH + "lcdImage.gif",
                        GALILEO_PATH + "drawNow/lcdImage.gif", ChannelSftp.OVERWRITE);
            } catch (SftpException ex) {
                Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSVGSendActionPerformed

    private void btnSVGClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVGClearActionPerformed
        if (SVGProcess.preview != null) {
            SVGProcess.preview.flush();
            SVGProcess.preview = null;
        }
        pSVGAction.setVisible(false);
        pSVGPreview.setIcon(null);
    }//GEN-LAST:event_btnSVGClearActionPerformed

    private void btnSVGProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVGProcessActionPerformed
        if (svgValidate.validate(this)) {
            int delay_X = Integer.parseInt(txtSVGDelayX.getText());
            int delay_Y = Integer.parseInt(txtSVGDelayY.getText());
            int delay_XY = Integer.parseInt(txtSVGDelayXY.getText());
            int scanLine = Integer.parseInt(cbSVGScanline.getSelectedItem().toString());
            int maxScanLine = Integer.parseInt(cbSVGMaxScanline.getSelectedItem().toString());
            Path.PRECISION = (Integer) sPrecision.getValue() / 100.0;
            Path.SEGMENTS = (Integer) sSegments.getValue();
            try {
                SVGProcess.proccess(delay_X, delay_Y, delay_XY, scanLine, maxScanLine);
            } catch (Exception ex) {
                ui.showError(ex);
                return;
            }

            Image img = new Image(SVGProcess.preview);
            img.newWidth = pSVGPreview.getWidth();
            img.newHeight = pSVGPreview.getHeight();
            img.resize(Scalr.Method.QUALITY, false);
            pSVGPreview.setIcon(new ImageIcon(img.image));
        }
    }//GEN-LAST:event_btnSVGProcessActionPerformed

    private void cbSVGScanlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSVGScanlineActionPerformed
        cbSVGMaxScanline.setEnabled(cbSVGScanline.getSelectedIndex() == 0);
    }//GEN-LAST:event_cbSVGScanlineActionPerformed

    private void pSVGPreviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pSVGPreviewMouseClicked
        if (SVGProcess.preview != null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Specify a file to save");
            fc.setFileFilter(new FileNameExtensionFilter("JPG, JPEG, GIF, PNG, BMP Images",
                    "jpg", "jpeg", "gif", "png", "bmp"));
            fc.setSelectedFile(new File(DRAWNOW_SVG_PATH + "preview.jpg"));
            int retVal = fc.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                try {
                    ImageIO.write(SVGProcess.preview, getFileExtension(fileToSave), fileToSave);
                } catch (IOException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_pSVGPreviewMouseClicked

    private void btnWatchRamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWatchRamActionPerformed
        term.sendCommand("watch -d free -m");
    }//GEN-LAST:event_btnWatchRamActionPerformed

    private void btnLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLSActionPerformed
        term.sendCommand("ls");
    }//GEN-LAST:event_btnLSActionPerformed

    private void termMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termMouseClicked
        term.requestFocus();
    }//GEN-LAST:event_termMouseClicked

    private void btnTestDrawNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestDrawNowActionPerformed
        jTabbedPaneMain.setEnabledAt(3, true);
        jTabbedPaneMain.setEnabledAt(4, true);
    }//GEN-LAST:event_btnTestDrawNowActionPerformed

    private void lblWidth1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblWidth1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblWidth1MouseClicked

    private void lblWidth2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblWidth2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblWidth2MouseClicked

    private void lblWidth3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblWidth3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblWidth3MouseClicked

    private void lblWidth4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblWidth4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblWidth4MouseClicked

    private String getFileExtension(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos == -1) {
            return "";
        }
        return name.substring(pos + 1);
    }

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            new GUIMain().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCDDir;
    private javax.swing.JButton btnCDDrawNow;
    private javax.swing.JButton btnCDLogs;
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnDrawNowClear;
    private javax.swing.JButton btnDrawNowOpen;
    private javax.swing.JButton btnDrawNowSend;
    private javax.swing.JButton btnGetLogs;
    private javax.swing.JButton btnLS;
    private javax.swing.JButton btnProcess;
    private javax.swing.JButton btnRMDrawNow;
    private javax.swing.JButton btnRMLog;
    private javax.swing.JButton btnRestart;
    private javax.swing.JButton btnSVGClear;
    private javax.swing.JButton btnSVGOpen;
    private javax.swing.JButton btnSVGProcess;
    private javax.swing.JButton btnSVGSend;
    private javax.swing.JButton btnSaveFile;
    private javax.swing.JButton btnSaveFolder;
    private javax.swing.JButton btnScreenRetach;
    private javax.swing.JButton btnShutdown;
    private javax.swing.JButton btnTermStart;
    private javax.swing.JButton btnTermStop;
    private javax.swing.JButton btnTestDrawNow;
    private javax.swing.JButton btnWatchRam;
    private javax.swing.JComboBox cbDrawNowQuality;
    private javax.swing.JComboBox cbSVGMaxScanline;
    private javax.swing.JComboBox cbSVGScanline;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTabbedPane jTabbedPanePreview;
    private javax.swing.JLabel lblFiles;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblPictureLog;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JLabel lblWidth1;
    private javax.swing.JLabel lblWidth2;
    private javax.swing.JLabel lblWidth3;
    private javax.swing.JLabel lblWidth4;
    private javax.swing.JList listFiles;
    private javax.swing.JList listLogs;
    private javax.swing.JPanel pAction;
    private javax.swing.JPanel pConnect;
    private javax.swing.JPanel pDrawNowAction;
    private javax.swing.JLabel pDrawNowPreview;
    private javax.swing.JPanel pLogs;
    private javax.swing.JPanel pSVGAction;
    private javax.swing.JLabel pSVGPreview;
    private javax.swing.JPanel pTerminal;
    private javax.swing.JSpinner sBrightness;
    private javax.swing.JSpinner sContrast;
    private javax.swing.JSpinner sPrecision;
    private javax.swing.JSpinner sSegments;
    private javax.swing.JTextArea taFileContent;
    private com.jcraft.jcterm.JCTermSwing term;
    private javax.swing.JTextField txtDrawNowDelayX;
    private javax.swing.JTextField txtDrawNowDelayY;
    private javax.swing.JTextField txtDrawNowHeight;
    private javax.swing.JTextField txtDrawNowWidth;
    private javax.swing.JTextField txtIPAddress;
    private javax.swing.JTextField txtLoginUser;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSVGDelayX;
    private javax.swing.JTextField txtSVGDelayXY;
    private javax.swing.JTextField txtSVGDelayY;
    // End of variables declaration//GEN-END:variables

    private final MyUserInfo ui = new MyUserInfo() {
        @Override
        public void showMessage(String message) {
            JOptionPane.showMessageDialog(GUIMain.this, message);
        }

        @Override
        public boolean promptYesNo(String message) {
            Object[] options = {"yes", "no"};
            int result = JOptionPane.showOptionDialog(GUIMain.this,
                    message, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return result == 0;
        }
    };

    private class MyProgressMonitor implements SftpProgressMonitor {

        private ProgressMonitor monitor;

        @Override
        public void init(int op, String src, String dest, long max) {
            monitor = new ProgressMonitor(null, "", "", 0, (int) max);
            monitor.setMillisToDecideToPopup(1000);
        }

        @Override
        public boolean count(long count) {
            monitor.setProgress(0);
            return true;
        }

        @Override
        public void end() {
            monitor.close();
        }
    }

    private abstract class MyUserInfo
            implements UserInfo, UIKeyboardInteractive {

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptYesNo(String str) {
            return false;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
        }

        public void showError(Exception ex) {
            showMessage("Error: " + ex.getMessage());
        }

        @Override
        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            return null;
        }
    }

}
