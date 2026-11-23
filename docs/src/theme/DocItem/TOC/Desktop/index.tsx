import React from 'react';
import TOCDesktop from '@theme-original/DocItem/TOC/Desktop';
import type TOCDesktopType from '@theme/DocItem/TOC/Desktop';
import type {WrapperProps} from '@docusaurus/types';
import SidebarImage from '@site/src/components/SidebarImage';
import styles from './styles.module.css';

type Props = WrapperProps<typeof TOCDesktopType>;

// The TOC plus the page's sidebar image, kept in view together while scrolling.
export default function TOCDesktopWrapper(props: Props) {
  return (
    <div className={styles.column}>
      <TOCDesktop {...props} />
      <SidebarImage placement="toc" />
    </div>
  );
}
