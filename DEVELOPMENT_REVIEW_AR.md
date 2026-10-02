# مراجعة AKALATY_EL_SHAHEYA وخطة التطوير

<!-- review-metadata -->
تاريخ المراجعة: 2026-10-02. فرع متابعة البناء: `codex/akalaty-build-2026-10-02`، من التغيير المراجع `d73d07b2e113cfdfc1e96439b18ecaad950b21d3`.

المصدر: [aymank2020/AKALATY_EL_SHAHEYA](https://github.com/aymank2020/AKALATY_EL_SHAHEYA)؛ commit الأساس: `7a036c4436a93b27d1ea89265a6d3fbf3c7cea9e`؛ عدد الملفات المتتبعة في الأساس: 82. Fork: false؛ مؤرشف: false.

نُفذت المرحلة المحددة أدناه بعد مراجعة الكود والاختبارات وتطبيق مراجعة التكامل والأثر؛ المراحل التالية والفجوات لا تُعد مكتملة.

المشروع تطبيق Android لعرض قوائم طعام من Firebase. فُحصت ملفات Gradle وManifest والنماذج والشاشات والموارد بصورة متكررة داخل الشجرة. يستخدم Android 29 وFirebaseUI Database 7.1.1؛ لا توجد منظومة طلبات مكتملة. اسم الحقل القديم `descrpition` محفوظ للتوافق مع بيانات Firebase.

## التنفيذ الحالي

- ربط مستمعي قائمتي الفئات والطعام بدورة حياة Activity عبر `setLifecycleOwner`، وحماية فتح القائمة عند غياب CategoryID أو المستخدم.
- عرض اسم الطعام وصورته وسعره ووصفه في FoodDetailsActivity من العنصر المختار فعليًا؛ كانت صفحة التفاصيل غير موصولة بالبيانات.
- تسجيل الدخول يقرأ سجل الهاتف المطلوب مرة واحدة، مع التحقق من الإدخال وحالات الفشل ومنع النقر المتكرر. التسجيل يستخدم transaction لمنع الكتابة فوق هاتف سبق تسجيله، ولا يعرض نجاحًا قبل تأكيد الكتابة.
- حراسة ردود Firebase بعد تدمير الشاشة وإغلاق نافذة الانتظار، وإضافة Maven Central قبل مستودع JCenter القديم.
- متابعة البناء: استبدال FirebaseUI Database 6.2.1 غير المتاح في المستودعات الحالية بالإصدار 7.1.1 المتاح في Maven Central، وحذف JCenter بعد نجاح حل جميع اعتماديات البناء من Google وMaven Central. يدعم الإصدار المختار Android 16 فأعلى وواجهة `setLifecycleOwner` المستخدمة؛ الحد الأدنى للتطبيق ما زال 17. يختار Gradle معه Firebase Database 19.5.1، بينما بقي Firebase Analytics 17.3.0.

## خطة المراحل التالية

1. أولوية حرجة: استبدال مقارنة كلمات المرور المخزنة بنظام Firebase Authentication، وربط UID بصلاحيات Realtime Database؛ يلزم انتقال بيانات وقواعد خدمة مدروسة، وليس تعديلًا محليًا منفردًا.
2. اختبار APK الذي أصبح يُبنى بنجاح: الدخول والتسجيل والتفاصيل على محاكي مع Firebase Emulator وبيانات اصطناعية، وتغطية تضارب إنشاء المستخدم وحالات الشبكة. الاختبار المحلي الحالي مجرد مثال للجمع، ولا يغطي هذه السلوكيات.
3. تصميم سلة وطلب واضح وحالة دفع قبل تنفيذ وظائف تجارية؛ ترقية Android وAGP على مراحل مع اختبارات توافق.

## التكامل والتحقق

مسار التطبيق الحقيقي: MainActivity → SignIn/SignUp → User في Firebase → Home → FoodList → FoodDetails. يستهلك Home وFoodList اعتماد FirebaseUI مباشرةً عبر `FirebaseRecyclerOptions` و`FirebaseRecyclerAdapter`، وتستخدم شاشة التفاصيل extras والموارد الموصولة في المصدر. هذه المسارات اجتازت التجميع؛ أثر المستمعين والمعاملات وتفاصيل الطعام على جهاز مع Firebase لم يُختبر بعد.

مسار إصلاح البناء: إعدادات Gradle → حل FirebaseUI 7.1.1 من Maven Central → تجميع Java ودمج الموارد وManifest → APK للتطبيق. محاولة المصدر السابق توقفت عند FirebaseUI 6.2.1؛ بعد التعديل نجح `assembleDebug testDebugUnitTest` باستخدام JDK8 وGradle 5.6.4 وAndroid SDK Platform 29 وBuild Tools 29.0.3، مع 27 مهمة منفذة. نجح اختبار محلي واحد، دون skipped أو failures. نجح `lintDebug` دون أخطاء، مع 56 تحذيرًا في التطبيق، منها الاعتماديات والهدف القديم وموارد غير مستخدمة وإعداد navigation.

فحص `aapt` للحزمة الناتجة أكد package `net.aymanx.ai.akalatyelsaheya`، ومدخل التشغيل `ui.MainActivity`، وminSdk 17 وtargetSdk 29. أكد `apksigner verify` صحة توقيعي v1 وv2. حجم APK هو 4,512,854 بايت؛ SHA256: `E3CDC98B9F192DEE1B9B66CB6FDDA29CD111E778EEAF09EC692593E9B18C2C6A`. الحزمة ومخرجات البناء محفوظة خارج Git، وليست إصدار نشر. تثبت هذه الفحوص إصلاح البناء وتكوين الحزمة، ولا تثبت نجاح الاستخدام على جهاز.

التوقف القديم عند تنزيل Build Tools كان سجلًا لمحاولة سابقة؛ الإصدار 29.0.3 موجود ويعمل في التحقق الحالي، لذلك لم يتغير تثبيته في المشروع. لم تُعدل إعدادات Firebase أو تُجرَ كتابة في خدمته الحقيقية. لا جهاز ADB متصل، ولا تحقق Firebase Emulator أو Android تفاعلي. التخزين الحالي لكلمات المرور ما زال فجوة حرجة، والتغييرات لا تجعل التطبيق جاهزًا للإنتاج.

## مصادر أولية

- [Firebase: القراءة والكتابة والمعاملات](https://firebase.google.com/docs/database/android/read-and-write)
- [FirebaseUI: دورة حياة القوائم](https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md)
- [Gradle: انتهاء JCenter](https://blog.gradle.org/jcenter-shutdown)
- [FirebaseUI 7.1.1: الإصدار الرسمي](https://github.com/firebase/FirebaseUI-Android/releases/tag/7.1.1)
- [FirebaseUI 7.1.1: الحد الأدنى وإصدارات Android](https://github.com/firebase/FirebaseUI-Android/blob/7.1.1/buildSrc/src/main/kotlin/Config.kt)
- [Maven Central: إصدارات FirebaseUI Database المتاحة](https://repo.maven.apache.org/maven2/com/firebaseui/firebase-ui-database/maven-metadata.xml)
