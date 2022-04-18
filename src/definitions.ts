export interface BrowserPlugin {
  create(options: BrowserOptions): void;
}

export interface BrowserOptions {
  /** 网页地址 */
  url: string
}