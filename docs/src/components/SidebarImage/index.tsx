import React from 'react';
import clsx from 'clsx';
import {useDoc} from '@docusaurus/plugin-content-docs/client';
import styles from './styles.module.css';

type Placement = 'toc' | 'inline';

// Renders the page's `sidebar_image` front matter (one URL or a list of them): under the
// table of contents on wide screens, and under the page title on narrow screens where the
// TOC column is hidden.
export default function SidebarImage({placement}: {placement: Placement}) {
  const {frontMatter, metadata} = useDoc();
  const value = (frontMatter as {sidebar_image?: string | string[]}).sidebar_image;
  const sources = value ? [value].flat() : [];
  if (sources.length === 0) return null;
  return (
    <figure className={clsx(styles.figure, styles[placement], sources.length > 1 && styles.multiple)}>
      {sources.map((src) => (
        <img key={src} src={src} alt={metadata.title} />
      ))}
    </figure>
  );
}
