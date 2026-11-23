import React, {useEffect, useState} from 'react';
import styles from './styles.module.css';

// Replaces Docusaurus's default BackToTopButton, which only appears while actively
// scrolling upward and disappears the moment you stop or scroll down again. This one
// just stays visible any time the page is scrolled past the threshold, and scrolls
// smoothly to the top on click.
const SHOW_THRESHOLD = 300;

export default function BackToTopButton() {
  const [shown, setShown] = useState(false);

  useEffect(() => {
    const onScroll = () => setShown(window.scrollY > SHOW_THRESHOLD);
    onScroll();
    window.addEventListener('scroll', onScroll, {passive: true});
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  return (
    <button
      type="button"
      aria-label="Scroll back to top"
      className={`${styles.backToTop} ${shown ? styles.shown : ''}`}
      onClick={() => window.scrollTo({top: 0, behavior: 'smooth'})}>
      <svg viewBox="0 0 24 24" width="1.1em" height="1.1em" aria-hidden="true">
        <path d="M12 6.5 L19 15 L16.5 15 L12 9.8 L7.5 15 L5 15 Z" fill="currentColor" />
      </svg>
    </button>
  );
}
