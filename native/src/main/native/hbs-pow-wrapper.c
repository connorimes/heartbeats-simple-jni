/**
 * Native JNI bindings for heartbeats-simple.
 *
 * @author Connor Imes
 * @date 2015-11-17
 */

#include <stdlib.h>
#include <jni.h>
#include <heartbeat-pow.h>
#include <hbs-pow-wrapper.h>

#define MACRO_GET_HB() \
  heartbeat_pow_context* hb = NULL; \
  if (ptr != NULL) { \
    hb = (heartbeat_pow_context*) (*env)->GetDirectBufferAddress(env, ptr); \
  }

/**
 * Allocate memory and get the heartbeat.
 * Returns a pointer to the heartbeat, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowInit(JNIEnv* env,
                                                                                           jobject obj,
                                                                                           jint window_size) {
  if (window_size <= 0) {
  	return NULL;
  }
  heartbeat_pow_context* hb = malloc(sizeof(heartbeat_pow_context));
  if (hb == NULL) {
    return NULL;
  }
  heartbeat_pow_record* hbr = malloc(window_size * sizeof(heartbeat_pow_record));
  if (hbr == NULL) {
  	free(hb);
  	return NULL;
  }
  if (heartbeat_pow_init(hb, window_size, hbr, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_pow_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPow(JNIEnv* env,
                                                                                    jobject obj,
                                                                                    jobject ptr,
                                                                                    jlong user_tag,
                                                                                    jlong work,
                                                                                    jlong start_time,
                                                                                    jlong end_time,
                                                                                    jlong start_energy,
                                                                                    jlong end_energy) {
  MACRO_GET_HB();
  heartbeat_pow(hb, user_tag, work, start_time, end_time, start_energy, end_energy);
}

/**
 * Cleanup the heartbeat specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowFinish(JNIEnv* env,
                                                                                          jobject obj,
                                                                                          jobject ptr) {
  MACRO_GET_HB();
  if (hb != NULL) {
    free(hb->window_buffer);
    free(hb);
  }
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowLogHeader(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jint fd) {
  return hb_pow_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowLogWindowBuffer(JNIEnv* env,
                                                                                                   jobject obj,
                                                                                                   jobject ptr,
                                                                                                   jint fd) {
  MACRO_GET_HB();
  return hb_pow_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowSize(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetUserTag(JNIEnv* env,
                                                                                               jobject obj,
                                                                                               jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetGlobalTime(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowTime(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetGlobalWork(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowWork(JNIEnv* env,
                                                                                                  jobject obj,
                                                                                                  jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetGlobalPerf(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowPerf(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetInstantPerf(JNIEnv* env,
                                                                                                     jobject obj,
                                                                                                     jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_instant_perf(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetGlobalEnergy(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_energy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowEnergy(JNIEnv* env,
                                                                                                    jobject obj,
                                                                                                    jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_energy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetGlobalPower(JNIEnv* env,
                                                                                                     jobject obj,
                                                                                                     jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetWindowPower(JNIEnv* env,
                                                                                                     jobject obj,
                                                                                                     jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeatPowGetInstantPower(JNIEnv* env,
                                                                                                      jobject obj,
                                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_instant_power(hb);
}
