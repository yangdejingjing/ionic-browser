import { WebPlugin } from '@capacitor/core';

import type { BrowserOptions, BrowserPlugin } from './definitions';

export class BrowserWeb extends WebPlugin implements BrowserPlugin {
  create(options: BrowserOptions): void {
    console.log(options);
    throw new Error('Method not implemented.');
  }
}
