'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { database } = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.guiThongBaoTinNhan = functions.database.ref('/Tin_Nhan/{pushId}').onWrite((change, context) => {

    const valueObject = change.after.val();


    let db = admin.firestore();
    var hoTen;
    const sfRef = db.collection('Users').doc(valueObject.nguoiGui);
    sfRef.get().then(function (doc) {
        const payload = {
            notification: {
                title: "Tin nhắn từ: " + doc.data().HoTen,
                body: valueObject.tinNhan,
                sound: "default"
            }
        };
        const options = {
            priority: "high",
            timeToLive: 60 * 60 * 24
        };
        return admin.messaging().sendToTopic(valueObject.nguoiNhan, payload, options);
    })

});
exports.guiThongBao = functions.database.ref('/Thong_Bao/{pushId}').onWrite((change, context) => {
    console.log('Push notification event triggered');

    //  Get the current value of what was written to the Realtime Database.
    const valueObject = change.after.val();
    let db = admin.firestore();
    var hoTen;
    const sfRef = db.collection('Users').doc(valueObject.uid);
    sfRef.get().then(function (doc) {
        const payload = {
            notification: {
                title: "Thông báo từ: " + doc.data().HoTen + " đến các Sinh viên",
                body: valueObject.thongBao,
                sound: "default"
            }
        };

        //Create an options object that contains the time to live for the notification and the priority
        const options = {
            priority: "high",
            timeToLive: 60 * 60 * 24
        };
        return admin.messaging().sendToTopic("ThongBaoSinhVien", payload, options);
    })
    // Create a notification

});



