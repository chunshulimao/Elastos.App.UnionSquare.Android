/*
 * Copyright (c) 2019 Elastos Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.elastos.wallet.ela.ElaWallet;

/**
 * Enums often require more than twice as much memory as static constants.
 * 程序会根据包名自动分配这些  请勿修改此文件
 */
public class WalletNet {
    //-1alpha 默认0正式  1testnet 2 regtest  3私有链
    // 新添加的net必须保证更下面的不一样
    public static final int ALPHAMAINNET = -1;// Mainnet分身特殊配置  为了预演MainNet
    public static final int MAINNET = 0;
    public static final int TESTNET = 1;
    public static final int REGTESTNET = 2;
    public static final int PRVNET = 3;

    //中心化服务器地址
    public static final String MAINURL = "https://unionsquare01.elastos.com.cn";//mainnet高可用
    public static final String MAINURL1 = "https://unionsquare.elastos.org";//mainnet高可用1
    public static final String TESTURL = "https://52.81.8.194:442";
    public static final String REGTESTURL = "http://54.223.244.60";
    public static final String PRVURL = "http://node.longrunweather.com:18080";

    //did resolve网址
    public static final String MAINDID = "http://api.elastos.io:20606";
    public static final String TESTDID = "http://api.elastos.io:21606";
    public static final String REGDID = "http://api.elastos.io:22606";
    public static final String PRIDID = "http://drpc.longrunweather.com:18080";

}
