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

#define MACRO_GET_HB() \
  heartbeat_acc_pow_context* hb = NULL; \
  if (ptr != NULL) { \
    hb = (heartbeat_acc_pow_context*) (*env)->GetDirectBufferAddress(env, ptr); \
  }

/**
 * Allocate memory and get the heartbeat.
 * Returns a pointer to the heartbeat, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_init(JNIEnv* env,
                                                                                  jobject obj,
                                                                                  jint window_size,
                                                                                  jint log_fd) {
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
  if (heartbeat_acc_pow_init(hb, window_size, hbr, log_fd, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_acc_pow_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_heartbeat(JNIEnv* env,
                                                                                    jobject obj,
                                                                                    jobject ptr,
                                                                                    jlong user_tag,
                                                                                    jlong work,
                                                                                    jlong start_time,
                                                                                    jlong end_time,
                                                                                    jlong accuracy,
                                                                                    jlong start_energy,
                                                                                    jlong end_energy) {
  MACRO_GET_HB();
  heartbeat_acc_pow(hb, user_tag, work, start_time, end_time, accuracy, start_energy, end_energy);
}

/**
 * Cleanup the heartbeat specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_finish(JNIEnv* env,
                                                                                 jobject obj,
                                                                                 jobject ptr) {
  MACRO_GET_HB();
  if (hb != NULL) {
    free(hb->window_buffer);
    free(hb);
  }
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_logHeader(JNIEnv* env,
                                                                                    jobject obj,
                                                                                    jint fd) {
  return hb_acc_pow_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_logWindowBuffer(JNIEnv* env,
                                                                                          jobject obj,
                                                                                          jobject ptr,
                                                                                          jint fd) {
  MACRO_GET_HB();
  return hb_acc_pow_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowSize(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getUserTag(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalTime(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowTime(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalWork(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowWork(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalPerf(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowPerf(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getInstantPerf(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_instant_perf(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalAccuracy(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_accuracy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowAccuracy(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_accuracy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalAccuracyRate(JNIEnv* env,
                                                                                                   jobject obj,
                                                                                                   jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowAccuracyRate(JNIEnv* env,
                                                                                                   jobject obj,
                                                                                                   jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_accuracy_rate(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getInstantAccuracyRate(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_instant_accuracy_rate(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalEnergy(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_energy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowEnergy(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_energy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getGlobalPower(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_global_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getWindowPower(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_window_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatAccPowJNI_getInstantPower(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jobject ptr) {
  MACRO_GET_HB();
  return hb_acc_pow_get_instant_power(hb);
}
