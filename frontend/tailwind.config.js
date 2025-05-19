/**@type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
        colors:{
            color:{
              blue: 'var(--color-blue)',
              yellow: 'var(--color-yellow)',
              backGround: 'var(--color-background)',
            }
        },
        spacing:{
          'sm': '0.25rem',
          'md': '0.5rem', 
          'lg': '1rem',
          'xl': '2rem', 
          '2xl': '3rem' 
        },
        padding:{
          'responsive-padding': 'clamp(0.75rem, 3vw, 2.5rem)'
        },
        fontSize:{
          'responsive-h1' : 'clamp(2.5rem, 3vw + 0.5rem, 4rem)',
          'responsive-h2' : 'clamp(1rem, 3vw + 0.5rem, 3rem)',
          'responsive-h3' : 'clamp(0.75rem, 3vw + 0.5rem, 1.8rem)',
          'responsive-p' : 'clamp(0.75rem, 3vw + 0.5rem, 1.25rem)'
        }
    },
  },
  plugins: [
      function({addComponents,theme}){
      const containers = {
        '.container-responsive': {
          width: '100%',
          margin: 'auto',
          '@screen sm': {
            minWidth: '640px',
          },
          '@screen md':{
            minWidth: '768px',
          },
          '@screen lg':{
            minWidth: '920px',
          },
          '@screen xl':{
            minWidth: '1250px',
          },
          '@screen 2xl':{
            minWidth: '1536px',
          }
        }
      }

      addComponents(containers);
    
    }
  ],
}