/**
 * Copyright Trimble Inc., 2014 - 2015 All rights reserved.
 *
 * Licensed Software Confidential and Proprietary Information of Trimble Inc.,
 * made available under Non-Disclosure Agreement OR License as applicable.
 *
 * Product Name:
 *      
 *
 * Module Name:
 *      com.neural.mock
 *
 * File name:
 *	    ISensorUpdateListener.java
 *
 * Author:
 *     sprabhu
 *
 * Created On:
 *     04-Feb-201512:41:37 am
 *
 * Abstract:
 *
 *
 * Environment:
 *  Mobile Profile          :
 *  Mobile Configuration    :
 *
 * Notes:
 *
 * Revision History:
 *
 *
 */
package com.balloongame.listeners;

/**
 * @author sprabhu
 *
 */
public interface ISensorUpdateListener{
   
   public static final int DEVICE_NONE         = 0;
   public static final int DEVICE_CONNECTED    = 1;
   public static final int DEVICE_DISCONNECTED = 2;
   public static final int DEVICE_PAIRED       = 3;
   public static final int DEVICE_UNPAIRED     = 4;

   public void onSensorDataUpdate(final double x,final double y);

   public void onSensorStatusUpdate(final int iStatusCode,final String stDeviceName,final String stNotificationTxt);

}