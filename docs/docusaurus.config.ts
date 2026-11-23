import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const repo = 'https://github.com/SirSquidly/Oceanic-Expanse';

const config: Config = {
  title: 'Oceanic Expanse',
  tagline: "Adds new content and expands upon Minecraft's Oceans.",
  favicon: 'img/logo.png',

  future: {
    v4: true,
  },

  url: 'https://SirSquidly.github.io',
  baseUrl: '/Oceanic-Expanse/',
  organizationName: 'SirSquidly',
  projectName: 'Oceanic-Expanse',

  onBrokenLinks: 'throw',

  // Pages migrated from the GitHub wiki use raw HTML (<img ...>, <br>) that is not valid MDX,
  // so .md files are parsed as plain CommonMark and only .mdx files go through MDX.
  markdown: {
    format: 'detect',
    hooks: {
      onBrokenMarkdownLinks: 'throw',
    },
  },

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  stylesheets: [
    'https://fonts.googleapis.com/css2?family=Silkscreen:wght@400;700&display=swap',
  ],

  themes: [
    [
      '@easyops-cn/docusaurus-search-local',
      {
        hashed: true,
        indexDocs: true,
        indexBlog: false,
        docsRouteBasePath: '/',
      },
    ],
  ],

  presets: [
    [
      'classic',
      {
        docs: {
          routeBasePath: '/',
          sidebarPath: './sidebars.ts',
          editUrl: `${repo}/tree/master/docs/`,
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    colorMode: {
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: 'Oceanic Expanse',
      logo: {
        alt: 'Oceanic Expanse logo',
        src: 'img/logo.png',
      },
      items: [
        {
          type: 'html',
          position: 'right',
          value:
            '<a href="https://www.curseforge.com/minecraft/mc-mods/oceanic-expanse" target="_blank" rel="noopener noreferrer" class="navbar-curseforge-link" aria-label="CurseForge" title="CurseForge">' +
            '<svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><path fill="currentColor" d="M18.326 9.2145S23.2261 8.4418 24 6.1882h-7.5066V4.4H0l2.0318 2.3576V9.173s5.1267 -0.2665 7.1098 1.2372c2.7146 2.516 -3.053 5.917 -3.053 5.917L5.0995 19.6c1.5465 -1.4726 4.494 -3.3775 9.8983 -3.2857 -2.0565 0.65 -4.1245 1.6651 -5.7344 3.2857h10.9248l-1.0288 -3.2726s-7.918 -4.6688 -0.8336 -7.1127z"/></svg>' +
            '</a>',
        },
        {
          type: 'html',
          position: 'right',
          value:
            '<a href="https://discord.gg/4kvU5D86An" target="_blank" rel="noopener noreferrer" class="navbar-discord-link" aria-label="Discord" title="Discord">' +
            '<svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><path fill="currentColor" d="M20.317 4.37a19.79 19.79 0 0 0-4.885-1.515.074.074 0 0 0-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 0 0-5.487 0 12.64 12.64 0 0 0-.617-1.25.077.077 0 0 0-.079-.037A19.736 19.736 0 0 0 3.677 4.37a.07.07 0 0 0-.032.027C.533 9.046-.32 13.58.099 18.057a.082.082 0 0 0 .031.056 19.9 19.9 0 0 0 5.993 3.03.078.078 0 0 0 .084-.028 14.09 14.09 0 0 0 1.226-1.994.076.076 0 0 0-.041-.106 13.107 13.107 0 0 1-1.872-.892.077.077 0 0 1-.008-.128 10.2 10.2 0 0 0 .372-.292.074.074 0 0 1 .077-.01c3.928 1.793 8.18 1.793 12.062 0a.074.074 0 0 1 .078.01c.12.098.246.198.373.292a.077.077 0 0 1-.006.127 12.3 12.3 0 0 1-1.873.892.076.076 0 0 0-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 0 0 .084.028 19.84 19.84 0 0 0 6.002-3.03.077.077 0 0 0 .032-.055c.5-5.177-.838-9.673-3.549-13.66a.061.061 0 0 0-.031-.028ZM8.02 15.33c-1.183 0-2.157-1.086-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.211 0 2.176 1.095 2.157 2.42 0 1.332-.955 2.418-2.157 2.418Zm7.975 0c-1.183 0-2.157-1.086-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.211 0 2.176 1.095 2.157 2.42 0 1.332-.946 2.418-2.157 2.418Z"/></svg>' +
            '</a>',
        },
        {
          href: repo,
          label: 'GitHub',
          position: 'right',
        },
        {
          href: `${repo}/issues`,
          label: 'Report an issue',
          position: 'right',
          className: 'navbar-report-issue-button',
        },
      ],
    },
    footer: {
      style: 'dark',
      logo: {
        alt: 'Oceanic Expanse',
        src: 'img/oe-banner.png',
      },
      copyright: `Copyright © ${new Date().getFullYear()} Sir Squidly, built with Docusaurus`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
