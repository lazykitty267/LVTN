package bk.lvtn;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bk.lvtn.Signature.DigitalSignature;
import dataService.DataService;
import dataService.OfflineDataService;
import entity.AttachImage;
import entity.PdfFile;
import entity.Report;

public class UploadOfflineService extends Service {
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    String strContent;
    int mNotificationId = 001;
    public UploadOfflineService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        strContent = "Đang đồng bộ các báo cáo ngoại tuyến";
        mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_done_black_24dp)
                        .setContentTitle("EASYREPORT")
                        .setContentText(strContent);


        // Sets id cho notification
        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
        resultIntent.putExtra("content", strContent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // Set content intent;
        mBuilder.setContentIntent(resultPendingIntent);
        // Let it continue running until it is stopped.
        OfflineDataService offData = new OfflineDataService();
        offData.doOpenDb(this);
        List<Report> listReport = offData.loadReport();
        DataService dataService = new DataService();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        for (Report report : listReport) {
            File userDir = new File(path,report.getId());
            PdfFile pdfFile = new PdfFile();
            pdfFile.setName(report.getId());
            File file = new File(userDir,report.getId()+".pdf");
            File attachDir = new File(userDir,report.getId());
//            User user = dataService.getCurrentUser(LoginActivity.this);
//            report.setUserName(user.getUsername());
            if(report.getNote().equals(OfflineDataService.CREATE_MODE))
                dataService.saveReport(report);
            else dataService.updateReport(report);
            if (attachDir.listFiles() != null) {
                for (File f : attachDir.listFiles()) {
                    AttachImage attachImage = new AttachImage();
                    attachImage.setReportId(report.getId());
                    attachImage.setName(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                    dataService.uploadAttachFile(f, attachImage);
                }
            }
            final DigitalSignature digi = new DigitalSignature();
            final File keyFileDirectory = new File(getFilesDir(), "rsa/");
            final File privateKeyFile = new File(keyFileDirectory, report.getUserName() + "_priv_key");
            final File publicKeyFile = new File(keyFileDirectory, "sikkr_pub_key");
            byte[] b = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(b);
                String k = digi.myhash(b);
                byte[] s = digi.rsaSign(k.getBytes(), digi.getPrivateKey(privateKeyFile));
                final File signal = new File(getFilesDir(), "signal");
                signal.createNewFile();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(signal));
                dos.write(s);
                dos.flush();
                dos.close();
                dataService.saveSignature(signal, pdfFile);
                signal.delete();

                pdfFile.setReportId(report.getId());
                dataService.uploadFile(file, pdfFile);

                offData.doDeleteDB(this);
            }
            catch (Exception e){
                mBuilder.setContentText("Xảy ra lỗi trong quá trình đồng bộ");
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                stopSelf();
                e.printStackTrace();
            }

            MediaPlayer mp;
            mp = MediaPlayer.create(this, R.raw.notiaudio);
            mp.start();
            mBuilder.setContentText("Đồng bộ thành công");
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            stopSelf();
//            Toast.makeText(LoginActivity.this, "OFFLINE MODE", Toast.LENGTH_SHORT);
        }
        return START_STICKY;
    }
}
