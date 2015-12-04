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
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_init(JNIEnv* env,
                                                                               jobject obj,
                                                                               jint window_size,
                                                                               jint log_fd) {
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
  if (heartbeat_pow_init(hb, window_size, hbr, log_fd, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_pow_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_heartbeat(JNIEnv* env,
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
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_free(JNIEnv* env,
                                                                            jobject obj,
                                                                            jobject ptr) {
  MACRO_GET_HB();
  if (hb != NULL) {
    free(hb->window_buffer);
    free(hb);
  }
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_logHeader(JNIEnv* env,
                                                                                 jobject obj,
                                                                                 jint fd) {
  return hb_pow_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_logWindowBuffer(JNIEnv* env,
                                                                                       jobject obj,
                                                                                       jobject ptr,
                                                                                       jint fd) {
  MACRO_GET_HB();
  return hb_pow_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowSize(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getUserTag(JNIEnv* env,
                                                                                   jobject obj,
                                                                                   jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getGlobalTime(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowTime(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getGlobalWork(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowWork(JNIEnv* env,
                                                                                      jobject obj,
                                                                                      jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getGlobalPerf(JNIEnv* env,
                                                                                        jobject obj,
                                                                                        jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowPerf(JNIEnv* env,
                                                                                        jobject obj,
                                                                                        jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getInstantPerf(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_instant_perf(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getGlobalEnergy(JNIEnv* env,
                                                                                        jobject obj,
                                                                                        jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_energy(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowEnergy(JNIEnv* env,
                                                                                        jobject obj,
                                                                                        jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_energy(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getGlobalPower(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_global_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getWindowPower(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_window_power(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatPowJNI_getInstantPower(JNIEnv* env,
                                                                                          jobject obj,
                                                                                          jobject ptr) {
  MACRO_GET_HB();
  return hb_pow_get_instant_power(hb);
}
