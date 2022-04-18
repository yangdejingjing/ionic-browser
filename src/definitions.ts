export interface BrowserPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
