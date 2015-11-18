/**
 * Native JNI bindings for heartbeats-simple.
 *
 * @author Connor Imes
 * @date 2015-11-17
 */

#include <stdlib.h>
#include <jni.h>
#include <heartbeat-acc-pow.h>
#include <hbs-acc-pow-wrapper.h>

#define MACRO_GET_HB_OR_FAIL() \
  if (ptr == NULL) { \
    return -1; \
  } \
  heartbeat_acc_pow_context* hb = (heartbeat_acc_pow_context*) (*env)->GetDirectBufferAddress(env, ptr); \
  if (hb == NULL) { \
    return -1; \
  }

/**
 * Allocate memory and get the heartbeat.
 * Returns a pointer to the heartbeat, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowInit(JNIEnv* env,
                                                                                                 jobject obj,
                                                                                                 jint window_size) {
  if (window_size <= 0) {
  	return NULL;
  }
  heartbeat_acc_pow_context* hb = malloc(sizeof(heartbeat_acc_pow_context));
  if (hb == NULL) {
    return NULL;
  }
  heartbeat_acc_pow_record* hbr = malloc(window_size * sizeof(heartbeat_acc_pow_record));
  if (hbr == NULL) {
  	free(hb);
  	return NULL;
  }
  if (heartbeat_acc_pow_init(hb, window_size, hbr, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_acc_pow_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT int JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPow(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr,
                                                                                         jlong user_tag,
                                                                                         jlong work,
                                                                                         jlong start_time,
                                                                                         jlong end_time,
                                                                                         jlong accuracy,
                                                                                         jlong start_energy,
                                                                                         jlong end_energy) {
  MACRO_GET_HB_OR_FAIL();
  heartbeat_acc_pow(hb, user_tag, work, start_time, end_time, accuracy, start_energy, end_energy);
  return 0;
}

/**
 * Cleanup the heartbeat specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowFinish(JNIEnv* env,
                                                                                                jobject obj,
                                                                                                jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  free(hb->window_buffer);
  free(hb);
  return 0;
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowLogHeader(JNIEnv* env,
                                                                                                   jobject obj,
                                                                                                   jint fd) {
  return hb_acc_pow_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowLogWindowBuffer(JNIEnv* env,
                                                                                                         jobject obj,
                                                                                                         jobject ptr,
                                                                                                         jint fd) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowSize(JNIEnv* env,
                                                                                                        jobject obj,
                                                                                                        jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetUserTag(JNIEnv* env,
                                                                                                     jobject obj,
                                                                                                     jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalTime(JNIEnv* env,
                                                                                                        jobject obj,
                                                                                                        jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowTime(JNIEnv* env,
                                                                                                        jobject obj,
                                                                                                        jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalWork(JNIEnv* env,
                                                                                                        jobject obj,
                                                                                                        jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowWork(JNIEnv* env,
                                                                                                        jobject obj,
                                                                                                        jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalPerf(JNIEnv* env,
                                                                                                          jobject obj,
                                                                                                          jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowPerf(JNIEnv* env,
                                                                                                          jobject obj,
                                                                                                          jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetInstantPerf(JNIEnv* env,
                                                                                                           jobject obj,
                                                                                                           jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_instant_perf(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalAccuracy(JNIEnv* env,
                                                                                                            jobject obj,
                                                                                                            jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_accuracy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowAccuracy(JNIEnv* env,
                                                                                                            jobject obj,
                                                                                                            jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_accuracy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalAccuracyRate(JNIEnv* env,
                                                                                                                  jobject obj,
                                                                                                                  jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowAccuracyRate(JNIEnv* env,
                                                                                                                  jobject obj,
                                                                                                                  jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetInstantAccuracyRate(JNIEnv* env,
                                                                                                                   jobject obj,
                                                                                                                   jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_instant_accuracy_rate(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalEnergy(JNIEnv* env,
                                                                                                          jobject obj,
                                                                                                          jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_energy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowEnergy(JNIEnv* env,
                                                                                                          jobject obj,
                                                                                                          jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_energy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetGlobalPower(JNIEnv* env,
                                                                                                           jobject obj,
                                                                                                           jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_global_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetWindowPower(JNIEnv* env,
                                                                                                           jobject obj,
                                                                                                           jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_window_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeatAccPowGetInstantPower(JNIEnv* env,
                                                                                                            jobject obj,
                                                                                                            jobject ptr) {
  MACRO_GET_HB_OR_FAIL();
  return hb_acc_pow_get_instant_power(hb);
}
