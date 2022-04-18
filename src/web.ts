import { WebPlugin } from '@capacitor/core';

import type { BrowserPlugin } from './definitions';

export class BrowserWeb extends WebPlugin implements BrowserPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
