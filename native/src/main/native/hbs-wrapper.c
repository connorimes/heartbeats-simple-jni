/**
 * Native JNI bindings for heartbeats-simple.
 *
 * @author Connor Imes
 * @date 2015-11-17
 */

#include <stdlib.h>
#include <jni.h>
#include <heartbeat.h>
#include <hbs-wrapper.h>

#define MACRO_GET_HB() \
  heartbeat_context* hb = NULL; \
  if (ptr != NULL) { \
    hb = (heartbeat_context*) (*env)->GetDirectBufferAddress(env, ptr); \
  }

/**
 * Allocate memory and get the heartbeat.
 * Returns a pointer to the heartbeat, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatInit(JNIEnv* env,
                                                                                     jobject obj,
                                                                                     jint window_size) {
  if (window_size <= 0) {
    return NULL;
  }
  heartbeat_context* hb = malloc(sizeof(heartbeat_context));
  if (hb == NULL) {
    return NULL;
  }
  heartbeat_record* hbr = malloc(window_size * sizeof(heartbeat_record));
  if (hbr == NULL) {
    free(hb);
    return NULL;
  }
  if (heartbeat_init(hb, window_size, hbr, NULL)) {
    free(hbr);
    free(hb);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) hb, sizeof(heartbeat_context));
}

/**
 * Issue a heartbeat.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeat(JNIEnv* env,
                                                                              jobject obj,
                                                                              jobject ptr,
                                                                              jlong user_tag,
                                                                              jlong work,
                                                                              jlong start_time,
                                                                              jlong end_time) {
  MACRO_GET_HB();
  heartbeat(hb, user_tag, work, start_time, end_time);
}

/**
 * Cleanup the heartbeat specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatFinish(JNIEnv* env,
                                                                                    jobject obj,
                                                                                    jobject ptr) {
  MACRO_GET_HB();
  if (hb != NULL) {
    free(hb->window_buffer);
    free(hb);
  }
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatLogHeader(JNIEnv* env,
                                                                                       jobject obj,
                                                                                       jint fd) {
  return hb_log_header(fd);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatLogWindowBuffer(JNIEnv* env,
                                                                                             jobject obj,
                                                                                             jobject ptr,
                                                                                             jint fd) {
  MACRO_GET_HB();
  return hb_log_window_buffer(hb, fd);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetWindowSize(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_get_window_size(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetUserTag(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_HB();
  return hb_get_user_tag(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetGlobalTime(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_get_global_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetWindowTime(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_get_window_time(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetGlobalWork(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_get_global_work(hb);
}

JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetWindowWork(JNIEnv* env,
                                                                                            jobject obj,
                                                                                            jobject ptr) {
  MACRO_GET_HB();
  return hb_get_window_work(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetGlobalPerf(JNIEnv* env,
                                                                                              jobject obj,
                                                                                              jobject ptr) {
  MACRO_GET_HB();
  return hb_get_global_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetWindowPerf(JNIEnv* env,
                                                                                              jobject obj,
                                                                                              jobject ptr) {
  MACRO_GET_HB();
  return hb_get_window_perf(hb);
}

JNIEXPORT jdouble JNICALL Java_edu_uchicago_cs_heartbeats_HeartbeatJNI_heartbeatGetInstantPerf(JNIEnv* env,
                                                                                               jobject obj,
                                                                                               jobject ptr) {
  MACRO_GET_HB();
  return hb_get_instant_perf(hb);
}
