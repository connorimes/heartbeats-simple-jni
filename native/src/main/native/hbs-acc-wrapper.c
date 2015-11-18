/**
 * Native JNI bindings for heartbeats-simple.
 *
 * @author Connor Imes
 * @date 2015-11-17
 */

#include <stdlib.h>
#include <jni.h>
#include <heartbeat-acc.h>
#include <hbs-acc-wrapper.h>

#define MACRO_GET_HB() \
  heartbeat_acc_context* hb = NULL; \
  if (ptr != NULL) { \
    hb = (heartbeat_acc_context*) (*env)->GetDirectBufferAddress(env, ptr); \
  }

/**
 * Allocate memory and get the heartbeat.
 * Returns a pointer to the heartbeat, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccInit(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jint window_size) {
  if (window_size <= 0) {
  	return NULL;
  }
  heartbeat_acc_context* hb = malloc(sizeof(heartbeat_acc_context));
  if (hb == NULL) {
    return NULL;
  }
  heartbeat_acc_record* hbr = malloc(window_size * sizeof(heartbeat_acc_record));
  if (hbr == NULL) {
  	free(hb);
  	return NULL;
  }
  if (heartbeat_acc_init(hb, window_size, hbr, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_acc_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAcc(JNIEnv* env,
                                                                                    jobject obj,
                                                                                    jobject ptr,
                                                                                    jlong user_tag,
                                                                                    jlong work,
                                                                                    jlong start_time,
                                                                                    jlong end_time,
                                                                                    jlong accuracy) {
  MACRO_GET_HB();
  heartbeat_acc(hb, user_tag, work, start_time, end_time, accuracy);
}

/**
 * Cleanup the heartbeat specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccFinish(JNIEnv* env,
                                                                                          jobject obj,
                                                                                          jobject ptr) {
  MACRO_GET_HB();
  if (hb == NULL) {
    return -1;
  }
  free(hb->window_buffer);
  free(hb);
  return 0;
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccLogHeader(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jint fd) {
  return hb_acc_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccLogWindowBuffer(JNIEnv* env,
                                                                                                   jobject obj,
                                                                                                   jobject ptr,
                                                                                                   jint fd) {
  MACRO_GET_HB();
  return hb_acc_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowSize(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetUserTag(JNIEnv* env,
                                                                                               jobject obj,
                                                                                               jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetGlobalTime(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowTime(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetGlobalWork(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowWork(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetGlobalPerf(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowPerf(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetInstantPerf(JNIEnv* env,
                                                                                                     jobject obj,
                                                                                                     jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_instant_perf(hb);
}


JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetGlobalAccuracy(JNIEnv* env,
                                                                                                      jobject obj,
                                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_global_accuracy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowAccuracy(JNIEnv* env,
                                                                                                      jobject obj,
                                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_accuracy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetGlobalAccuracyRate(JNIEnv* env,
                                                                                                            jobject obj,
                                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_global_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetWindowAccuracyRate(JNIEnv* env,
                                                                                                            jobject obj,
                                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_window_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccJNI_heartbeatAccGetInstantAccuracyRate(JNIEnv* env,
                                                                                                             jobject obj,
                                                                                                             jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_get_instant_accuracy_rate(hb);
}
