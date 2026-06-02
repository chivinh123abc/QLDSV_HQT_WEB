/**
 * ============================================================================
 * QLDSV_HTC — Client-Side Theme System (Abstract Factory Pattern)
 * ============================================================================
 */

// 1. Abstract Products
class ThemeIcon {
    getIconClass() { throw new Error("Method getIconClass() must be implemented."); }
}

class ThemeConfig {
    getThemeName() { throw new Error("Method getThemeName() must be implemented."); }
    getLabel() { throw new Error("Method getLabel() must be implemented."); }
}

// 2. Concrete Products for Light Mode
class LightThemeIcon extends ThemeIcon {
    getIconClass() {
        return "bi-sun-fill";
    }
}

class LightThemeConfig extends ThemeConfig {
    getThemeName() {
        return "light";
    }
    getLabel() {
        return "Chế độ Sáng";
    }
}

// 3. Concrete Products for Dark Mode
class DarkThemeIcon extends ThemeIcon {
    getIconClass() {
        return "bi-moon-stars-fill";
    }
}

class DarkThemeConfig extends ThemeConfig {
    getThemeName() {
        return "dark";
    }
    getLabel() {
        return "Chế độ Tối";
    }
}

// 4. Abstract Factory
class ThemeFactory {
    createIcon() { throw new Error("Method createIcon() must be implemented."); }
    createConfig() { throw new Error("Method createConfig() must be implemented."); }
}

// 5. Concrete Factories
class LightThemeFactory extends ThemeFactory {
    createIcon() {
        return new LightThemeIcon();
    }
    createConfig() {
        return new LightThemeConfig();
    }
}

class DarkThemeFactory extends ThemeFactory {
    createIcon() {
        return new DarkThemeIcon();
    }
    createConfig() {
        return new DarkThemeConfig();
    }
}

// 6. Theme Manager (Client)
class ThemeManager {
    constructor() {
        this.themeKey = "theme";
        this.currentTheme = localStorage.getItem(this.themeKey) || "light";
        this.factory = this.getFactory(this.currentTheme);
    }

    getFactory(themeName) {
        if (themeName === "dark") {
            return new DarkThemeFactory();
        }
        return new LightThemeFactory();
    }

    applyTheme() {
        const config = this.factory.createConfig();
        const icon = this.factory.createIcon();
        const themeName = config.getThemeName();

        // Apply attribute to document element
        document.documentElement.setAttribute("data-theme", themeName);

        // Update Toggle UI elements if they exist
        const toggleBtn = document.getElementById("themeToggleBtn");
        const toggleIcon = document.getElementById("themeToggleIcon");

        if (toggleIcon) {
            // Remove previous bootstrap icon classes
            toggleIcon.className = "bi " + icon.getIconClass() + " fs-5";
        }
        if (toggleBtn) {
            toggleBtn.setAttribute("title", config.getLabel());
            toggleBtn.setAttribute("aria-label", config.getLabel());
        }
    }

    toggleTheme() {
        this.currentTheme = this.currentTheme === "light" ? "dark" : "light";
        localStorage.setItem(this.themeKey, this.currentTheme);
        this.factory = this.getFactory(this.currentTheme);
        this.applyTheme();
    }

    init() {
        // Apply immediately
        this.applyTheme();

        const setupToggle = () => {
            const toggleBtn = document.getElementById("themeToggleBtn");
            if (toggleBtn) {
                // Remove clone if exists to avoid double bindings
                const newToggleBtn = toggleBtn.cloneNode(true);
                toggleBtn.parentNode.replaceChild(newToggleBtn, toggleBtn);
                
                newToggleBtn.addEventListener("click", (e) => {
                    e.preventDefault();
                    this.toggleTheme();
                });
            }
            this.applyTheme();
        };

        // Check if DOM is already fully loaded
        if (document.readyState === "loading") {
            document.addEventListener("DOMContentLoaded", setupToggle);
        } else {
            setupToggle();
        }
    }
}

// Initialize Theme System globally
const themeManager = new ThemeManager();
themeManager.init();
