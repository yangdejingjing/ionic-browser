import { Plugin } from "@capacitor/core";

export interface BrowserPlugin extends Plugin {
  create(options: BrowserOptions): void;
}

export interface BrowserOptions {
  /** 网页地址 */
  url: string,
  /** 主题，dark，light */
  theme?: string
}

/**
 * 分享到微信事件
 */
export interface BrowserShareToWechatEventOptions {
  /** 访问地址 */
  url: string,
  /** 缩略图base */
  thumb: string,
  /** 标题 */
  title: string
}